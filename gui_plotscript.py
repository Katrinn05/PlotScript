import sys
import os
import shutil
import tempfile
import re
import ctypes  # Used to suppress Windows error dialog on abort()
from PyQt5.QtCore import Qt, QProcess
from PyQt5.QtGui import QPixmap, QColor
from PyQt5.QtWidgets import (
    QApplication, QMainWindow, QWidget, QAction, QFileDialog,
    QLabel, QTextEdit, QTabWidget, QVBoxLayout, QHBoxLayout,
    QPushButton, QLineEdit, QTextBrowser, QSizePolicy,
    QMessageBox, QFormLayout, QGroupBox, QRadioButton, QButtonGroup,
    QSplitter, QColorDialog
)

def resource_path(relative_path: str) -> str:
    """
    Returns the absolute path to a resource when running under PyInstaller,
    or the normal relative path to the script in development mode.
    """
    if hasattr(sys, '_MEIPASS'):
        return os.path.join(sys._MEIPASS, relative_path)
    return os.path.join(os.path.abspath(os.path.dirname(__file__)), relative_path)

def find_plot_script_executable() -> str:
    """
    Attempts to locate the PlotScript executable (PlotScript.exe or equivalent).
    """
    base = os.path.abspath(os.path.dirname(__file__))
    if sys.platform.startswith("win"):
        candidates = [
            resource_path("PlotScript.exe"),
            os.path.join(base, "PlotScript.exe"),
            os.path.join(base, "build", "Debug", "PlotScript.exe"),
            os.path.join(base, "build", "Release", "PlotScript.exe"),
        ]
    elif sys.platform == "darwin":
        candidates = [
            resource_path("PlotScript"),
            os.path.join(base, "PlotScript"),
            os.path.join(base, "MacBuild", "PlotScript"),
            os.path.join(base, "build", "Debug", "PlotScript"),
            os.path.join(base, "build", "Release", "PlotScript"),
        ]
    else:
        candidates = [
            resource_path("PlotScript"),
            os.path.join(base, "PlotScript"),
            os.path.join(base, "build", "Debug", "PlotScript"),
            os.path.join(base, "build", "Release", "PlotScript"),
        ]

    for path in candidates:
        if os.path.isfile(path) and os.access(path, os.X_OK):
            return path
    return ""

def parse_output_name(plot_text: str) -> str:
    """
    Extracts the output filename from a .plot script (just the name, no path).
    Supports both single- and double-quoted syntax.
    """
    match = re.search(r'output\s*:\s*"([^"]+)"', plot_text, re.IGNORECASE)
    if match:
        return match.group(1).strip()
    match = re.search(r"output\s*:\s*'([^']+)'", plot_text, re.IGNORECASE)
    if match:
        return match.group(1).strip()
    return ""

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle("PlotScript GUI")
        self.resize(1000, 700)

        # Variables
        self.current_plot_path = ""     # path to the opened .plot file, if any
        self.generated_temp_path = None # path to a temporary .plot if generated
        self.output_image_path = ""     # path to the output image
        self.process = None             # QProcess for PlotScript
        self.current_pixmap = QPixmap() # loaded image pixmap
        self.scale_factor = 1.0         # zoom scale factor

        # Menu actions
        open_action = QAction("&Open .plot File...", self)
        open_action.setShortcut("Ctrl+O")
        open_action.triggered.connect(self.open_plot_file)

        save_action = QAction("&Save .plot As...", self)
        save_action.setShortcut("Ctrl+S")
        save_action.triggered.connect(self.save_plot_file_as)

        menubar = self.menuBar()
        file_menu = menubar.addMenu("&File")
        file_menu.addAction(open_action)
        file_menu.addAction(save_action)

        # Central widget with splitter
        central = QWidget()
        main_layout = QHBoxLayout(central)
        splitter = QSplitter(Qt.Horizontal)

        # Left frame: tabs + log
        left_widget = QWidget()
        left_layout = QVBoxLayout(left_widget)
        left_layout.setContentsMargins(5, 5, 5, 5)
        left_layout.setSpacing(10)

        self.tabs = QTabWidget()
        self.tabs.setTabPosition(QTabWidget.North)

        # Tab 1: File Editor
        self.tab_file = QWidget()
        file_layout = QVBoxLayout(self.tab_file)
        file_layout.setContentsMargins(0, 0, 0, 0)
        file_layout.setSpacing(5)

        self.editor = QTextEdit()
        self.editor.setPlaceholderText(
            "Type PlotScript code here (or open a .plot file above)"
        )
        file_layout.addWidget(self.editor)

        btn_run_file = QPushButton("Run")
        btn_run_file.clicked.connect(self.run_plotscript)
        file_layout.addWidget(btn_run_file, alignment=Qt.AlignRight)

        self.tabs.addTab(self.tab_file, "File Editor")

        # Tab 2: Generate from Params
        self.tab_generate = QWidget()
        gen_layout = QVBoxLayout(self.tab_generate)
        gen_layout.setContentsMargins(0, 0, 0, 0)
        gen_layout.setSpacing(5)

        form_group = QGroupBox("Chart Parameters")
        form_layout = QFormLayout(form_group)

        # Axis 1: raw PlotScript expression (user can type any valid grammar)
        self.input_axis1 = QLineEdit()
        self.input_axis1.setPlaceholderText(
            "e.g. [0,1,2,3] or input('data.txt') or arange(first(0), last(10), step(0.5f))"
        )
        form_layout.addRow("Axis 1 Expression:", self.input_axis1)

        # Axis 2: raw PlotScript expression or C++ function
        self.rb_axis2_vals = QRadioButton("Use Expression")
        self.rb_axis2_vals.setChecked(True)
        self.rb_axis2_cpp = QRadioButton("Use C++ Function")
        self.bg_axis2 = QButtonGroup()
        self.bg_axis2.addButton(self.rb_axis2_vals)
        self.bg_axis2.addButton(self.rb_axis2_cpp)
        form_layout.addRow(self.rb_axis2_vals)
        form_layout.addRow(self.rb_axis2_cpp)

        self.input_axis2 = QLineEdit()
        self.input_axis2.setPlaceholderText(
            "e.g. [0,2,4,6] or input('data2.txt') or arange(first(0), last(10), step(1f))"
        )
        form_layout.addRow("Axis 2 Expression:", self.input_axis2)

        self.input_axis2_cpp = QTextEdit()
        self.input_axis2_cpp.setPlaceholderText(
            "// Paste C++ code here (must define f(double x) and print one value per line):\n"
            "double f(double x) {\n"
            "    return x * x;\n"
            "}\n"
            "#include <iostream>\n"
            "using namespace std;\n"
            "int main() {\n"
            "    const int N = 5;\n"
            "    double xs[N] = {0, 1, 2, 3, 4};\n"
            "    for (int i = 0; i < N; ++i) {\n"
            "        cout << f(xs[i]);\n"
            "        if (i+1 < N) cout << '\\n';\n"
            "    }\n"
            "    return 0;\n"
            "}\n"
        )
        self.input_axis2_cpp.setVisible(False)
        form_layout.addRow(self.input_axis2_cpp)

        self.rb_axis2_vals.toggled.connect(self._toggle_axis2_input)

        # Color picker
        color_layout = QHBoxLayout()
        self.input_color = QLineEdit()
        self.input_color.setReadOnly(True)
        self.input_color.setPlaceholderText("Click 'Choose...' to pick color")
        btn_pick_color = QPushButton("Choose...")
        btn_pick_color.clicked.connect(self.pick_color)
        color_layout.addWidget(self.input_color)
        color_layout.addWidget(btn_pick_color)
        form_layout.addRow("Color (fill/line):", color_layout)

        # Axis scales
        self.input_xscale = QLineEdit()
        self.input_xscale.setPlaceholderText("e.g. 1.0")
        form_layout.addRow("Axis 1 Scale:", self.input_xscale)

        self.input_yscale = QLineEdit()
        self.input_yscale.setPlaceholderText("e.g. 1.0")
        form_layout.addRow("Axis 2 Scale:", self.input_yscale)

        # Output filename
        self.input_output = QLineEdit()
        self.input_output.setPlaceholderText("e.g. chart.png (required)")
        form_layout.addRow("Output Filename:", self.input_output)

        gen_layout.addWidget(form_group)

        btn_run_generate = QPushButton("Run")
        btn_run_generate.clicked.connect(self.run_plotscript)
        gen_layout.addWidget(btn_run_generate, alignment=Qt.AlignRight)

        self.tabs.addTab(self.tab_generate, "Generate from Params")

        left_layout.addWidget(self.tabs)

        # Execution log
        self.log = QTextBrowser()
        self.log.setReadOnly(True)
        self.log.setPlaceholderText("PlotScript execution log...")
        left_layout.addWidget(self.log, stretch=1)

        splitter.addWidget(left_widget := left_widget)  # noqa: F841
        splitter.setStretchFactor(0, 0)

        # Right frame: image preview + zoom + save
        right_widget = QWidget()
        right_layout = QVBoxLayout(right_widget)
        right_layout.setContentsMargins(5, 5, 5, 5)
        right_layout.setSpacing(5)

        zoom_layout = QHBoxLayout()
        btn_zoom_out = QPushButton("–")
        btn_zoom_out.setFixedSize(30, 30)
        btn_zoom_out.clicked.connect(self.zoom_out)
        btn_zoom_in = QPushButton("+")
        btn_zoom_in.setFixedSize(30, 30)
        btn_zoom_in.clicked.connect(self.zoom_in)
        zoom_layout.addStretch()
        zoom_layout.addWidget(btn_zoom_out)
        zoom_layout.addWidget(btn_zoom_in)
        zoom_layout.addStretch()
        right_layout.addLayout(zoom_layout)

        self.image_label = QLabel(alignment=Qt.AlignCenter)
        self.image_label.setText("Chart will appear here")
        self.image_label.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        self.image_label.setStyleSheet(
            "QLabel { background-color: #f0f0f0; border: 1px solid #ccc; }"
        )
        right_layout.addWidget(self.image_label)

        btn_save_image = QPushButton("Save Image As...")
        btn_save_image.clicked.connect(self.save_image_as)
        right_layout.addWidget(btn_save_image, alignment=Qt.AlignCenter)

        splitter.addWidget(right_widget)
        splitter.setStretchFactor(1, 1)

        main_layout.addWidget(splitter)
        self.setCentralWidget(central)

        self.statusBar().showMessage("Ready")

    def _toggle_axis2_input(self):
        """
        Toggles visibility of the Axis 2 input: either QLineEdit (expression) or QTextEdit (C++ code).
        """
        if self.rb_axis2_vals.isChecked():
            self.input_axis2.setVisible(True)
            self.input_axis2_cpp.setVisible(False)
        else:
            self.input_axis2.setVisible(False)
            self.input_axis2_cpp.setVisible(True)

    def pick_color(self):
        """
        Opens a QColorDialog and writes the selected color as "R,G,B" into QLineEdit.
        """
        color = QColorDialog.getColor()
        if color.isValid():
            r = color.red()
            g = color.green()
            b = color.blue()
            self.input_color.setText(f"{r},{g},{b}")

    def save_plot_file_as(self):
        """
        Saves the current editor content as a new .plot file.
        """
        fname, _ = QFileDialog.getSaveFileName(
            self, "Save .plot File As", "", "PlotScript Files (*.plot);;All Files (*)"
        )
        if not fname:
            return
        try:
            with open(fname, "w", encoding="utf-8") as f:
                f.write(self.editor.toPlainText())
            self.current_plot_path = fname
            self.statusBar().showMessage(f"Saved: {os.path.basename(fname)}")
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Cannot save file:\n{e}")

    def save_image_as(self):
        """
        Saves the current image (if any) under a different name/location.
        """
        if self.current_pixmap.isNull():
            QMessageBox.warning(self, "Warning", "No image to save.")
            return
        fname, _ = QFileDialog.getSaveFileName(
            self, "Save Image As", "", "PNG Files (*.png);;JPEG Files (*.jpg *.jpeg);;All Files (*)"
        )
        if not fname:
            return
        if not self.current_pixmap.save(fname):
            QMessageBox.critical(self, "Error", "Failed to save image.")
        else:
            self.statusBar().showMessage(f"Image saved as {os.path.basename(fname)}")

    def open_plot_file(self):
        """
        Opens a .plot file, loads its text into the editor,
        and if it contains an output field, attempts to display the generated image
        from the same folder.
        """
        fname, _ = QFileDialog.getOpenFileName(
            self, "Open .plot File", "", "PlotScript Files (*.plot);;All Files (*)"
        )
        if not fname:
            return

        try:
            with open(fname, "r", encoding="utf-8") as f:
                text = f.read()
        except Exception as e:
            QMessageBox.critical(self, "Error", f"Cannot open file:\n{e}")
            return

        self.current_plot_path = fname
        self.editor.setPlainText(text)
        self.statusBar().showMessage(f"Opened: {os.path.basename(fname)}")
        self.log.clear()

        out_name = parse_output_name(text)
        if out_name:
            plot_dir = os.path.dirname(fname)
            img_path = os.path.join(plot_dir, out_name)
            if os.path.isfile(img_path):
                self.output_image_path = img_path
                self._load_image_to_label()
            else:
                self.image_label.setText("No generated image found in the same folder")
        else:
            self.image_label.setText("No 'output' field in .plot file")

    def run_plotscript(self):
        """
        Called when clicking Run in either tab.
        - In File Editor tab, uses the saved .plot or editor content.
        - In Generate from Params tab, builds a .plot on the fly, then copies any
          'input("…")' files from the project root (current working directory)
          into the temp folder, and finally runs PlotScript.exe from that temp folder.
        """

        # 1) Clear log and image preview
        self.log.clear()
        self.image_label.clear()
        self.output_image_path = ""
        self.current_pixmap = QPixmap()
        self.scale_factor = 1.0

        # 2) Find PlotScript executable
        exe_path = find_plot_script_executable()
        if not exe_path:
            QMessageBox.critical(self, "Error", "Cannot locate PlotScript executable.")
            self.statusBar().showMessage("PlotScript not found")
            return

        plot_path_to_run = ""
        source_folder    = ""   # folder where we'll look for data.txt (project root)
        exec_folder      = ""   # folder from which we will actually launch PlotScript

        # 3) Which tab is active?
        if self.tabs.currentWidget() == self.tab_file:
            # ── File Editor tab ──
            code_text = self.editor.toPlainText().strip()
            if not code_text and not self.current_plot_path:
                QMessageBox.warning(self, "Warning", "Enter PlotScript code or open a .plot file.")
                self.statusBar().showMessage("No code or file")
                return

            if self.current_plot_path:
                # Save changes to the open .plot
                try:
                    with open(self.current_plot_path, "w", encoding="utf-8") as f:
                        f.write(code_text)
                except Exception as e:
                    QMessageBox.critical(self, "Error", f"Cannot save file:\n{e}")
                    self.statusBar().showMessage("Save error")
                    return

                plot_path_to_run = self.current_plot_path
                # If the user opened “somefolder\myplot.plot”, then data files must also live there
                source_folder = os.path.dirname(self.current_plot_path)
                exec_folder   = source_folder

                out_name = parse_output_name(code_text)
                if out_name:
                    self.output_image_path = os.path.join(source_folder, out_name)
                else:
                    self.output_image_path = ""

            else:
                # No saved .plot yet → create a temp .plot
                try:
                    temp = tempfile.NamedTemporaryFile(
                        mode='w', suffix=".plot", delete=False, encoding="utf-8"
                    )
                    temp.write(code_text)
                    temp.close()
                    self.generated_temp_path = temp.name
                    plot_path_to_run = self.generated_temp_path

                    # Use current working directory (project root) as source_folder.
                    source_folder = os.getcwd()
                    exec_folder   = os.path.dirname(self.generated_temp_path)

                    out_name = parse_output_name(code_text)
                    if out_name:
                        self.output_image_path = os.path.join(exec_folder, out_name)
                    else:
                        self.output_image_path = ""
                except Exception as e:
                    QMessageBox.critical(self, "Error", f"Cannot create temp .plot:\n{e}")
                    self.statusBar().showMessage("Temp file error")
                    return

        else:
            # ── Generate from Params tab ──
            axis1_text = self.input_axis1.text().strip()
            color_text = self.input_color.text().strip()
            xscale_text = self.input_xscale.text().strip()
            yscale_text = self.input_yscale.text().strip()
            output_text = self.input_output.text().strip()

            # 3.1) Validate Axis1
            if not axis1_text:
                QMessageBox.warning(self, "Warning", "Enter Axis 1 expression.")
                self.statusBar().showMessage("Axis1 missing")
                return
            axis1_block = axis1_text

            # 3.2) Build Axis2 block
            if self.rb_axis2_vals.isChecked():
                axis2_text = self.input_axis2.text().strip()
                if not axis2_text:
                    QMessageBox.warning(self, "Warning", "Enter Axis 2 expression.")
                    self.statusBar().showMessage("Axis2 missing")
                    return
                axis2_block = axis2_text
            else:
                cpp_code = self.input_axis2_cpp.toPlainText().strip()
                if not cpp_code:
                    QMessageBox.warning(self, "Warning", "Enter C++ code for Axis 2.")
                    self.statusBar().showMessage("No C++ code")
                    return
                axis2_block = "func(\n    $CPP$\n" + cpp_code + "\n    $$\n)"

            # 3.3) Build color_block (optional)
            color_block = ""
            if color_text:
                rgb_parts = [c.strip() for c in color_text.split(",") if c.strip()]
                if len(rgb_parts) != 3:
                    QMessageBox.warning(self, "Warning", 
                                        "Color must be 3 comma-separated values (0–255).")
                    self.statusBar().showMessage("Invalid Color")
                    return
                try:
                    rgb = [int(v) for v in rgb_parts]
                    if any((v < 0 or v > 255) for v in rgb):
                        raise ValueError
                except Exception:
                    QMessageBox.warning(self, "Warning", 
                                        "Color components must be integers 0–255.")
                    self.statusBar().showMessage("Invalid Color")
                    return
                color_block = f"  color: [{rgb[0]}, {rgb[1]}, {rgb[2]}];\n"

            # 3.4) Build xscale_block (optional)
            xscale_block = ""
            if xscale_text:
                try:
                    xv = float(xscale_text)
                    if xv <= 0:
                        raise ValueError
                    xscale_block = f"  axis1-scale: {xv};\n"
                except Exception:
                    QMessageBox.warning(self, "Warning", 
                                        "Axis 1 scale must be a positive number.")
                    self.statusBar().showMessage("Invalid X scale")
                    return

            # 3.5) Build yscale_block (optional)
            yscale_block = ""
            if yscale_text:
                try:
                    yv = float(yscale_text)
                    if yv <= 0:
                        raise ValueError
                    yscale_block = f"  axis2-scale: {yv};\n"
                except Exception:
                    QMessageBox.warning(self, "Warning", 
                                        "Axis 2 scale must be a positive number.")
                    self.statusBar().showMessage("Invalid Y scale")
                    return

            # 3.6) Validate output filename
            if not output_text:
                QMessageBox.warning(self, "Warning", 
                                    "Enter output filename (e.g. chart.png).")
                self.statusBar().showMessage("Output missing")
                return

            # 3.7) Assemble the .plot content
            plot_content = "myPlot {\n"
            plot_content += f"  axis1: {axis1_block};\n"
            plot_content += f"  axis2: {axis2_block};\n"
            plot_content += xscale_block
            plot_content += yscale_block
            plot_content += color_block
            plot_content += f"  output: '{output_text}';\n"
            plot_content += "}\n"

            try:
                temp = tempfile.NamedTemporaryFile(
                    mode='w', suffix=".plot", delete=False, encoding="utf-8"
                )
                temp.write(plot_content)
                temp.close()
                self.generated_temp_path = temp.name
                plot_path_to_run = self.generated_temp_path
                exec_folder = os.path.dirname(self.generated_temp_path)

                # Use the current working directory (project root) as source_folder.
                source_folder = os.getcwd()
                self.output_image_path = os.path.join(exec_folder, output_text)
            except Exception as e:
                QMessageBox.critical(self, "Error", f"Cannot create temp .plot:\n{e}")
                self.statusBar().showMessage("Temp file error")
                return

        # ───────────── Copy data files if needed ─────────────
        try:
            with open(plot_path_to_run, "r", encoding="utf-8") as f:
                final_plot_text = f.read()
        except Exception as e:
            QMessageBox.critical(self, "Error", 
                                 f"Cannot re-open .plot to scan for data inputs:\n{e}")
            return

        pattern = re.compile(r"input\s*\(\s*['\"]([^'\"]+)['\"]\s*\)")
        data_files = set(m.group(1) for m in pattern.finditer(final_plot_text))

        for data_path in data_files:
            # If data_path is absolute and exists, use it
            if os.path.isabs(data_path) and os.path.isfile(data_path):
                abs_data = data_path
            else:
                # Try relative to source_folder (current working directory)
                candidate = os.path.join(source_folder, data_path)
                if os.path.isfile(candidate):
                    abs_data = candidate
                else:
                    # Finally, try literal relative to current working directory
                    c2 = os.path.abspath(data_path)
                    if os.path.isfile(c2):
                        abs_data = c2
                    else:
                        QMessageBox.critical(self, "Error", f"Data file not found:\n{data_path}")
                        return

            # Destination for the data file in exec_folder
            dest = os.path.join(exec_folder, os.path.basename(data_path))
            # If source and destination are identical, skip copy
            if os.path.abspath(abs_data) == os.path.abspath(dest):
                continue
            try:
                shutil.copyfile(abs_data, dest)
            except Exception as copy_err:
                QMessageBox.critical(self, "Error", 
                                     f"Cannot copy '{abs_data}' to execution folder:\n{copy_err}")
                return

        # ───────────── Run PlotScript ─────────────
        if sys.platform.startswith("win"):
            SEM_NOGPFAULTERRORBOX = 0x0002
            ctypes.windll.kernel32.SetErrorMode(SEM_NOGPFAULTERRORBOX)

        plot_dir = os.path.dirname(plot_path_to_run)
        if not os.path.isdir(plot_dir):
            QMessageBox.critical(self, "Error", f"Cannot find directory: {plot_dir}")
            return

        self.statusBar().showMessage("Running PlotScript...")
        if self.process:
            self.process.kill()
            self.process = None

        self.process = QProcess(self)
        self.process.setWorkingDirectory(plot_dir)
        self.process.readyReadStandardOutput.connect(self._read_stdout)
        self.process.readyReadStandardError.connect(self._read_stderr)
        self.process.finished.connect(self._process_finished)

        self.process.start(exe_path, [plot_path_to_run])
        if not self.process.waitForStarted():
            QMessageBox.critical(self, "Error", "Failed to launch PlotScript.")
            self.statusBar().showMessage("Launch error")
            return

    def _read_stdout(self):
        """
        Reads stdout from PlotScript and appends it to the log.
        """
        if not self.process:
            return
        data = self.process.readAllStandardOutput().data().decode(errors='ignore')
        self.log.append(data)

    def _read_stderr(self):
        """
        Reads stderr from PlotScript and appends it to the log (in red).
        """
        if not self.process:
            return
        data = self.process.readAllStandardError().data().decode(errors='ignore')
        if data:
            self.log.append(f"<span style='color:red;'>{data}</span>")

    def _process_finished(self, exitCode: int, exitStatus):
        """
        After PlotScript finishes: if exitCode == 0 and the image exists, display it;
        otherwise report an error.
        """
        if exitCode == 0:
            if self.output_image_path and os.path.isfile(self.output_image_path):
                self._load_image_to_label()
                self.statusBar().showMessage("Done successfully")
            else:
                self.log.append(f"<span style='color:red;'><b>Image not found: {self.output_image_path}</b></span>")
                self.statusBar().showMessage("Image missing")
        else:
            self.statusBar().showMessage(f"Finished with error (code {exitCode})")

    def _load_image_to_label(self):
        """
        Loads the generated image into the QLabel and resets zoom.
        """
        pix = QPixmap(self.output_image_path)
        if pix.isNull():
            self.log.append(f"<span style='color:red;'><b>Cannot load image: {self.output_image_path}</b></span>")
            return
        self.current_pixmap = pix
        self.scale_factor = 1.0
        self.update_image_display()

    def update_image_display(self):
        """
        Scales current_pixmap by scale_factor and sets it in QLabel.
        """
        if self.current_pixmap.isNull():
            return
        orig_size = self.current_pixmap.size()
        new_w = int(orig_size.width() * self.scale_factor)
        new_h = int(orig_size.height() * self.scale_factor)
        scaled = self.current_pixmap.scaled(new_w, new_h, Qt.KeepAspectRatio, Qt.SmoothTransformation)
        self.image_label.setPixmap(scaled)

    def zoom_in(self):
        """
        Zoom in by a factor of 1.25×.
        """
        if self.current_pixmap.isNull():
            return
        self.scale_factor *= 1.25
        self.update_image_display()

    def zoom_out(self):
        """
        Zoom out by a factor of 1/1.25× (minimum of 0.1×).
        """
        if self.current_pixmap.isNull():
            return
        self.scale_factor /= 1.25
        if self.scale_factor < 0.1:
            self.scale_factor = 0.1
        self.update_image_display()

def main():
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())

if __name__ == "__main__":
    main()
