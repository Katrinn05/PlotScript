// Generated from d:/AGH/SEM4/KOMPILATORY/project/PlotScript/PlotScriptParser.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class PlotScriptParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PLOT_LBRACKET=1, PLOT_RBRACKET=2, LIST_LBRACKET=3, LIST_RBRACKET=4, FUNC_CALL_LBRACKET=5, 
		FUNC_CALL_RBRACKET=6, BLOCK_DELIMITER=7, VALUE_DELIMITER=8, ASSIGN=9, 
		AXIS1=10, AXIS2=11, COLOR=12, OUTPUT=13, ARRANGE=14, INPUT=15, AXIS1_SCALE=16, 
		AXIS2_SCALE=17, FUNC=18, FIRST=19, LAST=20, STEP=21, EXPORT=22, CPP_FUNC_START=23, 
		PY_FUNC_START=24, STRING=25, NUMBER=26, ID=27, WS=28, CPP_FUNC_END=29, 
		CPP_TYPE_INT=30, CPP_TYPE_DOUBLE=31, CPP_TYPE_BOOL=32, CPP_TYPE_VOID=33, 
		CPP_PLUS=34, CPP_MINUS=35, CPP_STAR=36, CPP_DIV=37, CPP_ASSIGN=38, CPP_COMMA=39, 
		CPP_SEMI=40, CPP_LPAREN=41, CPP_RPAREN=42, CPP_LBRACE=43, CPP_RBRACE=44, 
		CPP_IF=45, CPP_ELSE=46, CPP_FOR=47, CPP_RETURN=48, CPP_AND=49, CPP_OR=50, 
		CPP_NOT=51, CPP_NUMBER=52, CPP_ID=53, CPP_TRUE_KW=54, CPP_FALSE_KW=55, 
		CPP_WS=56, CPP_LINE_COMMENT=57, CPP_BLOCK_COMMENT=58, PY_DEF=59, PY_IMPORT=60, 
		PY_AS=61, PY_PASS=62, PY_TRUE_KW=63, PY_FALSE_KW=64, PY_NONE_KW=65, PY_COLON=66, 
		PY_PLUS=67, PY_MINUS=68, PY_STAR=69, PY_DIV=70, PY_ASSIGN=71, PY_LPAREN=72, 
		PY_RPAREN=73, PY_COMMA=74, PY_NUMBER=75, PY_ID=76, PY_WS=77, PY_LINE_COMMENT=78;
	public static final int
		RULE_program = 0, RULE_plotDefinition = 1, RULE_plotName = 2, RULE_plotBlock = 3, 
		RULE_plotStatement = 4, RULE_plotFunctionIdentifier = 5, RULE_expression = 6, 
		RULE_value = 7, RULE_list = 8, RULE_functionCall = 9, RULE_stringLikeFunction = 10, 
		RULE_stringLikeFunctionParam = 11, RULE_rangeArgs = 12, RULE_embeddedFunctionBlock = 13, 
		RULE_cpp_funcDeclaration = 14, RULE_cpp_funcReturnType = 15, RULE_cpp_typeSpecifier = 16, 
		RULE_cpp_paramList = 17, RULE_cpp_parameter = 18, RULE_cpp_funcBody = 19, 
		RULE_cpp_funcStatement = 20, RULE_cpp_assignment = 21, RULE_cpp_varDeclaration = 22, 
		RULE_cpp_return = 23, RULE_cpp_controlStructure = 24, RULE_cpp_expr = 25, 
		RULE_exportStatement = 26;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "plotDefinition", "plotName", "plotBlock", "plotStatement", 
			"plotFunctionIdentifier", "expression", "value", "list", "functionCall", 
			"stringLikeFunction", "stringLikeFunctionParam", "rangeArgs", "embeddedFunctionBlock", 
			"cpp_funcDeclaration", "cpp_funcReturnType", "cpp_typeSpecifier", "cpp_paramList", 
			"cpp_parameter", "cpp_funcBody", "cpp_funcStatement", "cpp_assignment", 
			"cpp_varDeclaration", "cpp_return", "cpp_controlStructure", "cpp_expr", 
			"exportStatement"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'['", "']'", null, null, null, null, null, null, null, 
			"'color'", "'output'", "'arrange'", "'input'", null, null, "'func'", 
			"'first'", "'last'", "'step'", "'export'", "'$CPP$'", "'$PY$'", null, 
			null, null, null, "'$$'", "'int'", null, "'bool'", "'void'", null, null, 
			null, null, null, null, null, null, null, null, null, "'if'", "'else'", 
			"'for'", "'return'", "'&&'", "'||'", "'!'", null, null, "'true'", "'false'", 
			null, null, null, "'def'", "'import'", "'as'", "'pass'", "'True'", "'False'", 
			"'None'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PLOT_LBRACKET", "PLOT_RBRACKET", "LIST_LBRACKET", "LIST_RBRACKET", 
			"FUNC_CALL_LBRACKET", "FUNC_CALL_RBRACKET", "BLOCK_DELIMITER", "VALUE_DELIMITER", 
			"ASSIGN", "AXIS1", "AXIS2", "COLOR", "OUTPUT", "ARRANGE", "INPUT", "AXIS1_SCALE", 
			"AXIS2_SCALE", "FUNC", "FIRST", "LAST", "STEP", "EXPORT", "CPP_FUNC_START", 
			"PY_FUNC_START", "STRING", "NUMBER", "ID", "WS", "CPP_FUNC_END", "CPP_TYPE_INT", 
			"CPP_TYPE_DOUBLE", "CPP_TYPE_BOOL", "CPP_TYPE_VOID", "CPP_PLUS", "CPP_MINUS", 
			"CPP_STAR", "CPP_DIV", "CPP_ASSIGN", "CPP_COMMA", "CPP_SEMI", "CPP_LPAREN", 
			"CPP_RPAREN", "CPP_LBRACE", "CPP_RBRACE", "CPP_IF", "CPP_ELSE", "CPP_FOR", 
			"CPP_RETURN", "CPP_AND", "CPP_OR", "CPP_NOT", "CPP_NUMBER", "CPP_ID", 
			"CPP_TRUE_KW", "CPP_FALSE_KW", "CPP_WS", "CPP_LINE_COMMENT", "CPP_BLOCK_COMMENT", 
			"PY_DEF", "PY_IMPORT", "PY_AS", "PY_PASS", "PY_TRUE_KW", "PY_FALSE_KW", 
			"PY_NONE_KW", "PY_COLON", "PY_PLUS", "PY_MINUS", "PY_STAR", "PY_DIV", 
			"PY_ASSIGN", "PY_LPAREN", "PY_RPAREN", "PY_COMMA", "PY_NUMBER", "PY_ID", 
			"PY_WS", "PY_LINE_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PlotScriptParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PlotScriptParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(PlotScriptParser.EOF, 0); }
		public List<PlotDefinitionContext> plotDefinition() {
			return getRuleContexts(PlotDefinitionContext.class);
		}
		public PlotDefinitionContext plotDefinition(int i) {
			return getRuleContext(PlotDefinitionContext.class,i);
		}
		public ExportStatementContext exportStatement() {
			return getRuleContext(ExportStatementContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(54);
				plotDefinition();
				}
				}
				setState(59);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXPORT) {
				{
				setState(60);
				exportStatement();
				}
			}

			setState(63);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PlotDefinitionContext extends ParserRuleContext {
		public PlotNameContext plotName() {
			return getRuleContext(PlotNameContext.class,0);
		}
		public PlotBlockContext plotBlock() {
			return getRuleContext(PlotBlockContext.class,0);
		}
		public PlotDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plotDefinition; }
	}

	public final PlotDefinitionContext plotDefinition() throws RecognitionException {
		PlotDefinitionContext _localctx = new PlotDefinitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_plotDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			plotName();
			setState(66);
			plotBlock();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PlotNameContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(PlotScriptParser.ID, 0); }
		public PlotNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plotName; }
	}

	public final PlotNameContext plotName() throws RecognitionException {
		PlotNameContext _localctx = new PlotNameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_plotName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PlotBlockContext extends ParserRuleContext {
		public TerminalNode PLOT_LBRACKET() { return getToken(PlotScriptParser.PLOT_LBRACKET, 0); }
		public TerminalNode PLOT_RBRACKET() { return getToken(PlotScriptParser.PLOT_RBRACKET, 0); }
		public List<PlotStatementContext> plotStatement() {
			return getRuleContexts(PlotStatementContext.class);
		}
		public PlotStatementContext plotStatement(int i) {
			return getRuleContext(PlotStatementContext.class,i);
		}
		public PlotBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plotBlock; }
	}

	public final PlotBlockContext plotBlock() throws RecognitionException {
		PlotBlockContext _localctx = new PlotBlockContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_plotBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			match(PLOT_LBRACKET);
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 211968L) != 0)) {
				{
				{
				setState(71);
				plotStatement();
				}
				}
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(77);
			match(PLOT_RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PlotStatementContext extends ParserRuleContext {
		public PlotFunctionIdentifierContext plotFunctionIdentifier() {
			return getRuleContext(PlotFunctionIdentifierContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(PlotScriptParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode BLOCK_DELIMITER() { return getToken(PlotScriptParser.BLOCK_DELIMITER, 0); }
		public PlotStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plotStatement; }
	}

	public final PlotStatementContext plotStatement() throws RecognitionException {
		PlotStatementContext _localctx = new PlotStatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_plotStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			plotFunctionIdentifier();
			setState(80);
			match(ASSIGN);
			setState(81);
			expression();
			setState(82);
			match(BLOCK_DELIMITER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PlotFunctionIdentifierContext extends ParserRuleContext {
		public TerminalNode AXIS1() { return getToken(PlotScriptParser.AXIS1, 0); }
		public TerminalNode AXIS2() { return getToken(PlotScriptParser.AXIS2, 0); }
		public TerminalNode COLOR() { return getToken(PlotScriptParser.COLOR, 0); }
		public TerminalNode OUTPUT() { return getToken(PlotScriptParser.OUTPUT, 0); }
		public TerminalNode AXIS1_SCALE() { return getToken(PlotScriptParser.AXIS1_SCALE, 0); }
		public TerminalNode AXIS2_SCALE() { return getToken(PlotScriptParser.AXIS2_SCALE, 0); }
		public PlotFunctionIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plotFunctionIdentifier; }
	}

	public final PlotFunctionIdentifierContext plotFunctionIdentifier() throws RecognitionException {
		PlotFunctionIdentifierContext _localctx = new PlotFunctionIdentifierContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_plotFunctionIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 211968L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_expression);
		try {
			setState(89);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING:
			case NUMBER:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(86);
				value();
				}
				break;
			case LIST_LBRACKET:
				enterOuterAlt(_localctx, 2);
				{
				setState(87);
				list();
				}
				break;
			case ARRANGE:
			case INPUT:
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(88);
				functionCall();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(PlotScriptParser.STRING, 0); }
		public TerminalNode NUMBER() { return getToken(PlotScriptParser.NUMBER, 0); }
		public TerminalNode ID() { return getToken(PlotScriptParser.ID, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 234881024L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ListContext extends ParserRuleContext {
		public TerminalNode LIST_LBRACKET() { return getToken(PlotScriptParser.LIST_LBRACKET, 0); }
		public TerminalNode LIST_RBRACKET() { return getToken(PlotScriptParser.LIST_RBRACKET, 0); }
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public List<TerminalNode> VALUE_DELIMITER() { return getTokens(PlotScriptParser.VALUE_DELIMITER); }
		public TerminalNode VALUE_DELIMITER(int i) {
			return getToken(PlotScriptParser.VALUE_DELIMITER, i);
		}
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			match(LIST_LBRACKET);
			{
			setState(94);
			value();
			setState(99);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VALUE_DELIMITER) {
				{
				{
				setState(95);
				match(VALUE_DELIMITER);
				setState(96);
				value();
				}
				}
				setState(101);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			setState(102);
			match(LIST_RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public TerminalNode ARRANGE() { return getToken(PlotScriptParser.ARRANGE, 0); }
		public TerminalNode FUNC_CALL_LBRACKET() { return getToken(PlotScriptParser.FUNC_CALL_LBRACKET, 0); }
		public RangeArgsContext rangeArgs() {
			return getRuleContext(RangeArgsContext.class,0);
		}
		public TerminalNode FUNC_CALL_RBRACKET() { return getToken(PlotScriptParser.FUNC_CALL_RBRACKET, 0); }
		public StringLikeFunctionContext stringLikeFunction() {
			return getRuleContext(StringLikeFunctionContext.class,0);
		}
		public StringLikeFunctionParamContext stringLikeFunctionParam() {
			return getRuleContext(StringLikeFunctionParamContext.class,0);
		}
		public TerminalNode FUNC() { return getToken(PlotScriptParser.FUNC, 0); }
		public EmbeddedFunctionBlockContext embeddedFunctionBlock() {
			return getRuleContext(EmbeddedFunctionBlockContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_functionCall);
		try {
			setState(119);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ARRANGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(104);
				match(ARRANGE);
				setState(105);
				match(FUNC_CALL_LBRACKET);
				setState(106);
				rangeArgs();
				setState(107);
				match(FUNC_CALL_RBRACKET);
				}
				break;
			case INPUT:
				enterOuterAlt(_localctx, 2);
				{
				setState(109);
				stringLikeFunction();
				setState(110);
				match(FUNC_CALL_LBRACKET);
				setState(111);
				stringLikeFunctionParam();
				setState(112);
				match(FUNC_CALL_RBRACKET);
				}
				break;
			case FUNC:
				enterOuterAlt(_localctx, 3);
				{
				setState(114);
				match(FUNC);
				setState(115);
				match(FUNC_CALL_LBRACKET);
				setState(116);
				embeddedFunctionBlock();
				setState(117);
				match(FUNC_CALL_RBRACKET);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringLikeFunctionContext extends ParserRuleContext {
		public TerminalNode INPUT() { return getToken(PlotScriptParser.INPUT, 0); }
		public StringLikeFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLikeFunction; }
	}

	public final StringLikeFunctionContext stringLikeFunction() throws RecognitionException {
		StringLikeFunctionContext _localctx = new StringLikeFunctionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_stringLikeFunction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(INPUT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringLikeFunctionParamContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(PlotScriptParser.STRING, 0); }
		public StringLikeFunctionParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLikeFunctionParam; }
	}

	public final StringLikeFunctionParamContext stringLikeFunctionParam() throws RecognitionException {
		StringLikeFunctionParamContext _localctx = new StringLikeFunctionParamContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_stringLikeFunctionParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RangeArgsContext extends ParserRuleContext {
		public TerminalNode FIRST() { return getToken(PlotScriptParser.FIRST, 0); }
		public List<TerminalNode> FUNC_CALL_LBRACKET() { return getTokens(PlotScriptParser.FUNC_CALL_LBRACKET); }
		public TerminalNode FUNC_CALL_LBRACKET(int i) {
			return getToken(PlotScriptParser.FUNC_CALL_LBRACKET, i);
		}
		public List<TerminalNode> NUMBER() { return getTokens(PlotScriptParser.NUMBER); }
		public TerminalNode NUMBER(int i) {
			return getToken(PlotScriptParser.NUMBER, i);
		}
		public List<TerminalNode> FUNC_CALL_RBRACKET() { return getTokens(PlotScriptParser.FUNC_CALL_RBRACKET); }
		public TerminalNode FUNC_CALL_RBRACKET(int i) {
			return getToken(PlotScriptParser.FUNC_CALL_RBRACKET, i);
		}
		public List<TerminalNode> VALUE_DELIMITER() { return getTokens(PlotScriptParser.VALUE_DELIMITER); }
		public TerminalNode VALUE_DELIMITER(int i) {
			return getToken(PlotScriptParser.VALUE_DELIMITER, i);
		}
		public TerminalNode LAST() { return getToken(PlotScriptParser.LAST, 0); }
		public TerminalNode STEP() { return getToken(PlotScriptParser.STEP, 0); }
		public RangeArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeArgs; }
	}

	public final RangeArgsContext rangeArgs() throws RecognitionException {
		RangeArgsContext _localctx = new RangeArgsContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_rangeArgs);
		try {
			setState(148);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(125);
				match(FIRST);
				setState(126);
				match(FUNC_CALL_LBRACKET);
				setState(127);
				match(NUMBER);
				setState(128);
				match(FUNC_CALL_RBRACKET);
				setState(129);
				match(VALUE_DELIMITER);
				setState(130);
				match(LAST);
				setState(131);
				match(FUNC_CALL_LBRACKET);
				setState(132);
				match(NUMBER);
				setState(133);
				match(FUNC_CALL_RBRACKET);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(134);
				match(FIRST);
				setState(135);
				match(FUNC_CALL_LBRACKET);
				setState(136);
				match(NUMBER);
				setState(137);
				match(FUNC_CALL_RBRACKET);
				setState(138);
				match(VALUE_DELIMITER);
				setState(139);
				match(LAST);
				setState(140);
				match(FUNC_CALL_LBRACKET);
				setState(141);
				match(NUMBER);
				setState(142);
				match(FUNC_CALL_RBRACKET);
				setState(143);
				match(VALUE_DELIMITER);
				setState(144);
				match(STEP);
				setState(145);
				match(FUNC_CALL_LBRACKET);
				setState(146);
				match(NUMBER);
				setState(147);
				match(FUNC_CALL_RBRACKET);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmbeddedFunctionBlockContext extends ParserRuleContext {
		public TerminalNode CPP_FUNC_START() { return getToken(PlotScriptParser.CPP_FUNC_START, 0); }
		public Cpp_funcDeclarationContext cpp_funcDeclaration() {
			return getRuleContext(Cpp_funcDeclarationContext.class,0);
		}
		public TerminalNode CPP_FUNC_END() { return getToken(PlotScriptParser.CPP_FUNC_END, 0); }
		public EmbeddedFunctionBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_embeddedFunctionBlock; }
	}

	public final EmbeddedFunctionBlockContext embeddedFunctionBlock() throws RecognitionException {
		EmbeddedFunctionBlockContext _localctx = new EmbeddedFunctionBlockContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_embeddedFunctionBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150);
			match(CPP_FUNC_START);
			setState(151);
			cpp_funcDeclaration();
			setState(152);
			match(CPP_FUNC_END);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_funcDeclarationContext extends ParserRuleContext {
		public Cpp_funcReturnTypeContext cpp_funcReturnType() {
			return getRuleContext(Cpp_funcReturnTypeContext.class,0);
		}
		public TerminalNode CPP_ID() { return getToken(PlotScriptParser.CPP_ID, 0); }
		public TerminalNode CPP_LPAREN() { return getToken(PlotScriptParser.CPP_LPAREN, 0); }
		public TerminalNode CPP_RPAREN() { return getToken(PlotScriptParser.CPP_RPAREN, 0); }
		public Cpp_funcBodyContext cpp_funcBody() {
			return getRuleContext(Cpp_funcBodyContext.class,0);
		}
		public Cpp_paramListContext cpp_paramList() {
			return getRuleContext(Cpp_paramListContext.class,0);
		}
		public Cpp_funcDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_funcDeclaration; }
	}

	public final Cpp_funcDeclarationContext cpp_funcDeclaration() throws RecognitionException {
		Cpp_funcDeclarationContext _localctx = new Cpp_funcDeclarationContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_cpp_funcDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			cpp_funcReturnType();
			setState(155);
			match(CPP_ID);
			setState(156);
			match(CPP_LPAREN);
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 7516192768L) != 0)) {
				{
				setState(157);
				cpp_paramList();
				}
			}

			setState(160);
			match(CPP_RPAREN);
			setState(161);
			cpp_funcBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_funcReturnTypeContext extends ParserRuleContext {
		public TerminalNode CPP_TYPE_VOID() { return getToken(PlotScriptParser.CPP_TYPE_VOID, 0); }
		public Cpp_typeSpecifierContext cpp_typeSpecifier() {
			return getRuleContext(Cpp_typeSpecifierContext.class,0);
		}
		public Cpp_funcReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_funcReturnType; }
	}

	public final Cpp_funcReturnTypeContext cpp_funcReturnType() throws RecognitionException {
		Cpp_funcReturnTypeContext _localctx = new Cpp_funcReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_cpp_funcReturnType);
		try {
			setState(165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CPP_TYPE_VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				match(CPP_TYPE_VOID);
				}
				break;
			case CPP_TYPE_INT:
			case CPP_TYPE_DOUBLE:
			case CPP_TYPE_BOOL:
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				cpp_typeSpecifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_typeSpecifierContext extends ParserRuleContext {
		public TerminalNode CPP_TYPE_INT() { return getToken(PlotScriptParser.CPP_TYPE_INT, 0); }
		public TerminalNode CPP_TYPE_DOUBLE() { return getToken(PlotScriptParser.CPP_TYPE_DOUBLE, 0); }
		public TerminalNode CPP_TYPE_BOOL() { return getToken(PlotScriptParser.CPP_TYPE_BOOL, 0); }
		public Cpp_typeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_typeSpecifier; }
	}

	public final Cpp_typeSpecifierContext cpp_typeSpecifier() throws RecognitionException {
		Cpp_typeSpecifierContext _localctx = new Cpp_typeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_cpp_typeSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 7516192768L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_paramListContext extends ParserRuleContext {
		public List<Cpp_parameterContext> cpp_parameter() {
			return getRuleContexts(Cpp_parameterContext.class);
		}
		public Cpp_parameterContext cpp_parameter(int i) {
			return getRuleContext(Cpp_parameterContext.class,i);
		}
		public List<TerminalNode> CPP_COMMA() { return getTokens(PlotScriptParser.CPP_COMMA); }
		public TerminalNode CPP_COMMA(int i) {
			return getToken(PlotScriptParser.CPP_COMMA, i);
		}
		public Cpp_paramListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_paramList; }
	}

	public final Cpp_paramListContext cpp_paramList() throws RecognitionException {
		Cpp_paramListContext _localctx = new Cpp_paramListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_cpp_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			cpp_parameter();
			setState(174);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CPP_COMMA) {
				{
				{
				setState(170);
				match(CPP_COMMA);
				setState(171);
				cpp_parameter();
				}
				}
				setState(176);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_parameterContext extends ParserRuleContext {
		public Cpp_typeSpecifierContext cpp_typeSpecifier() {
			return getRuleContext(Cpp_typeSpecifierContext.class,0);
		}
		public TerminalNode CPP_ID() { return getToken(PlotScriptParser.CPP_ID, 0); }
		public Cpp_parameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_parameter; }
	}

	public final Cpp_parameterContext cpp_parameter() throws RecognitionException {
		Cpp_parameterContext _localctx = new Cpp_parameterContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_cpp_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			cpp_typeSpecifier();
			setState(178);
			match(CPP_ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_funcBodyContext extends ParserRuleContext {
		public TerminalNode CPP_LBRACE() { return getToken(PlotScriptParser.CPP_LBRACE, 0); }
		public TerminalNode CPP_RBRACE() { return getToken(PlotScriptParser.CPP_RBRACE, 0); }
		public List<Cpp_funcStatementContext> cpp_funcStatement() {
			return getRuleContexts(Cpp_funcStatementContext.class);
		}
		public Cpp_funcStatementContext cpp_funcStatement(int i) {
			return getRuleContext(Cpp_funcStatementContext.class,i);
		}
		public Cpp_funcBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_funcBody; }
	}

	public final Cpp_funcBodyContext cpp_funcBody() throws RecognitionException {
		Cpp_funcBodyContext _localctx = new Cpp_funcBodyContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_cpp_funcBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			match(CPP_LBRACE);
			setState(184);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 9464603608088576L) != 0)) {
				{
				{
				setState(181);
				cpp_funcStatement();
				}
				}
				setState(186);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(187);
			match(CPP_RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_funcStatementContext extends ParserRuleContext {
		public Cpp_varDeclarationContext cpp_varDeclaration() {
			return getRuleContext(Cpp_varDeclarationContext.class,0);
		}
		public TerminalNode CPP_SEMI() { return getToken(PlotScriptParser.CPP_SEMI, 0); }
		public Cpp_assignmentContext cpp_assignment() {
			return getRuleContext(Cpp_assignmentContext.class,0);
		}
		public Cpp_returnContext cpp_return() {
			return getRuleContext(Cpp_returnContext.class,0);
		}
		public Cpp_controlStructureContext cpp_controlStructure() {
			return getRuleContext(Cpp_controlStructureContext.class,0);
		}
		public Cpp_funcStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_funcStatement; }
	}

	public final Cpp_funcStatementContext cpp_funcStatement() throws RecognitionException {
		Cpp_funcStatementContext _localctx = new Cpp_funcStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_cpp_funcStatement);
		try {
			setState(199);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CPP_TYPE_INT:
			case CPP_TYPE_DOUBLE:
			case CPP_TYPE_BOOL:
				enterOuterAlt(_localctx, 1);
				{
				setState(189);
				cpp_varDeclaration();
				setState(190);
				match(CPP_SEMI);
				}
				break;
			case CPP_ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(192);
				cpp_assignment();
				setState(193);
				match(CPP_SEMI);
				}
				break;
			case CPP_RETURN:
				enterOuterAlt(_localctx, 3);
				{
				setState(195);
				cpp_return();
				setState(196);
				match(CPP_SEMI);
				}
				break;
			case CPP_IF:
			case CPP_FOR:
				enterOuterAlt(_localctx, 4);
				{
				setState(198);
				cpp_controlStructure();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_assignmentContext extends ParserRuleContext {
		public TerminalNode CPP_ID() { return getToken(PlotScriptParser.CPP_ID, 0); }
		public TerminalNode CPP_ASSIGN() { return getToken(PlotScriptParser.CPP_ASSIGN, 0); }
		public Cpp_exprContext cpp_expr() {
			return getRuleContext(Cpp_exprContext.class,0);
		}
		public Cpp_assignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_assignment; }
	}

	public final Cpp_assignmentContext cpp_assignment() throws RecognitionException {
		Cpp_assignmentContext _localctx = new Cpp_assignmentContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_cpp_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(CPP_ID);
			setState(202);
			match(CPP_ASSIGN);
			setState(203);
			cpp_expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_varDeclarationContext extends ParserRuleContext {
		public Cpp_typeSpecifierContext cpp_typeSpecifier() {
			return getRuleContext(Cpp_typeSpecifierContext.class,0);
		}
		public TerminalNode CPP_ID() { return getToken(PlotScriptParser.CPP_ID, 0); }
		public TerminalNode CPP_ASSIGN() { return getToken(PlotScriptParser.CPP_ASSIGN, 0); }
		public Cpp_exprContext cpp_expr() {
			return getRuleContext(Cpp_exprContext.class,0);
		}
		public Cpp_varDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_varDeclaration; }
	}

	public final Cpp_varDeclarationContext cpp_varDeclaration() throws RecognitionException {
		Cpp_varDeclarationContext _localctx = new Cpp_varDeclarationContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_cpp_varDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			cpp_typeSpecifier();
			setState(206);
			match(CPP_ID);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CPP_ASSIGN) {
				{
				setState(207);
				match(CPP_ASSIGN);
				setState(208);
				cpp_expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_returnContext extends ParserRuleContext {
		public TerminalNode CPP_RETURN() { return getToken(PlotScriptParser.CPP_RETURN, 0); }
		public Cpp_exprContext cpp_expr() {
			return getRuleContext(Cpp_exprContext.class,0);
		}
		public Cpp_returnContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_return; }
	}

	public final Cpp_returnContext cpp_return() throws RecognitionException {
		Cpp_returnContext _localctx = new Cpp_returnContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_cpp_return);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			match(CPP_RETURN);
			setState(213);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 69807993247498240L) != 0)) {
				{
				setState(212);
				cpp_expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_controlStructureContext extends ParserRuleContext {
		public TerminalNode CPP_IF() { return getToken(PlotScriptParser.CPP_IF, 0); }
		public TerminalNode CPP_LPAREN() { return getToken(PlotScriptParser.CPP_LPAREN, 0); }
		public Cpp_exprContext cpp_expr() {
			return getRuleContext(Cpp_exprContext.class,0);
		}
		public TerminalNode CPP_RPAREN() { return getToken(PlotScriptParser.CPP_RPAREN, 0); }
		public List<Cpp_funcBodyContext> cpp_funcBody() {
			return getRuleContexts(Cpp_funcBodyContext.class);
		}
		public Cpp_funcBodyContext cpp_funcBody(int i) {
			return getRuleContext(Cpp_funcBodyContext.class,i);
		}
		public TerminalNode CPP_ELSE() { return getToken(PlotScriptParser.CPP_ELSE, 0); }
		public TerminalNode CPP_FOR() { return getToken(PlotScriptParser.CPP_FOR, 0); }
		public List<TerminalNode> CPP_SEMI() { return getTokens(PlotScriptParser.CPP_SEMI); }
		public TerminalNode CPP_SEMI(int i) {
			return getToken(PlotScriptParser.CPP_SEMI, i);
		}
		public List<Cpp_assignmentContext> cpp_assignment() {
			return getRuleContexts(Cpp_assignmentContext.class);
		}
		public Cpp_assignmentContext cpp_assignment(int i) {
			return getRuleContext(Cpp_assignmentContext.class,i);
		}
		public Cpp_controlStructureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_controlStructure; }
	}

	public final Cpp_controlStructureContext cpp_controlStructure() throws RecognitionException {
		Cpp_controlStructureContext _localctx = new Cpp_controlStructureContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_cpp_controlStructure);
		int _la;
		try {
			setState(239);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CPP_IF:
				enterOuterAlt(_localctx, 1);
				{
				setState(215);
				match(CPP_IF);
				setState(216);
				match(CPP_LPAREN);
				setState(217);
				cpp_expr(0);
				setState(218);
				match(CPP_RPAREN);
				setState(219);
				cpp_funcBody();
				setState(222);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CPP_ELSE) {
					{
					setState(220);
					match(CPP_ELSE);
					setState(221);
					cpp_funcBody();
					}
				}

				}
				break;
			case CPP_FOR:
				enterOuterAlt(_localctx, 2);
				{
				setState(224);
				match(CPP_FOR);
				setState(225);
				match(CPP_LPAREN);
				setState(227);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CPP_ID) {
					{
					setState(226);
					cpp_assignment();
					}
				}

				setState(229);
				match(CPP_SEMI);
				setState(231);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 69807993247498240L) != 0)) {
					{
					setState(230);
					cpp_expr(0);
					}
				}

				setState(233);
				match(CPP_SEMI);
				setState(235);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CPP_ID) {
					{
					setState(234);
					cpp_assignment();
					}
				}

				setState(237);
				match(CPP_RPAREN);
				setState(238);
				cpp_funcBody();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cpp_exprContext extends ParserRuleContext {
		public TerminalNode CPP_LPAREN() { return getToken(PlotScriptParser.CPP_LPAREN, 0); }
		public List<Cpp_exprContext> cpp_expr() {
			return getRuleContexts(Cpp_exprContext.class);
		}
		public Cpp_exprContext cpp_expr(int i) {
			return getRuleContext(Cpp_exprContext.class,i);
		}
		public TerminalNode CPP_RPAREN() { return getToken(PlotScriptParser.CPP_RPAREN, 0); }
		public TerminalNode CPP_ID() { return getToken(PlotScriptParser.CPP_ID, 0); }
		public TerminalNode CPP_NUMBER() { return getToken(PlotScriptParser.CPP_NUMBER, 0); }
		public TerminalNode CPP_NOT() { return getToken(PlotScriptParser.CPP_NOT, 0); }
		public TerminalNode CPP_TRUE_KW() { return getToken(PlotScriptParser.CPP_TRUE_KW, 0); }
		public TerminalNode CPP_FALSE_KW() { return getToken(PlotScriptParser.CPP_FALSE_KW, 0); }
		public TerminalNode CPP_PLUS() { return getToken(PlotScriptParser.CPP_PLUS, 0); }
		public TerminalNode CPP_MINUS() { return getToken(PlotScriptParser.CPP_MINUS, 0); }
		public TerminalNode CPP_STAR() { return getToken(PlotScriptParser.CPP_STAR, 0); }
		public TerminalNode CPP_DIV() { return getToken(PlotScriptParser.CPP_DIV, 0); }
		public TerminalNode CPP_AND() { return getToken(PlotScriptParser.CPP_AND, 0); }
		public TerminalNode CPP_OR() { return getToken(PlotScriptParser.CPP_OR, 0); }
		public Cpp_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpp_expr; }
	}

	public final Cpp_exprContext cpp_expr() throws RecognitionException {
		return cpp_expr(0);
	}

	private Cpp_exprContext cpp_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Cpp_exprContext _localctx = new Cpp_exprContext(_ctx, _parentState);
		Cpp_exprContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_cpp_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CPP_LPAREN:
				{
				setState(242);
				match(CPP_LPAREN);
				setState(243);
				cpp_expr(0);
				setState(244);
				match(CPP_RPAREN);
				}
				break;
			case CPP_ID:
				{
				setState(246);
				match(CPP_ID);
				}
				break;
			case CPP_NUMBER:
				{
				setState(247);
				match(CPP_NUMBER);
				}
				break;
			case CPP_NOT:
				{
				setState(248);
				match(CPP_NOT);
				setState(249);
				cpp_expr(3);
				}
				break;
			case CPP_TRUE_KW:
				{
				setState(250);
				match(CPP_TRUE_KW);
				}
				break;
			case CPP_FALSE_KW:
				{
				setState(251);
				match(CPP_FALSE_KW);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(262);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(260);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
					case 1:
						{
						_localctx = new Cpp_exprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cpp_expr);
						setState(254);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(255);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 257698037760L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(256);
						cpp_expr(9);
						}
						break;
					case 2:
						{
						_localctx = new Cpp_exprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_cpp_expr);
						setState(257);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(258);
						_la = _input.LA(1);
						if ( !(_la==CPP_AND || _la==CPP_OR) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(259);
						cpp_expr(5);
						}
						break;
					}
					} 
				}
				setState(264);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExportStatementContext extends ParserRuleContext {
		public TerminalNode EXPORT() { return getToken(PlotScriptParser.EXPORT, 0); }
		public TerminalNode FUNC_CALL_LBRACKET() { return getToken(PlotScriptParser.FUNC_CALL_LBRACKET, 0); }
		public List<PlotNameContext> plotName() {
			return getRuleContexts(PlotNameContext.class);
		}
		public PlotNameContext plotName(int i) {
			return getRuleContext(PlotNameContext.class,i);
		}
		public TerminalNode FUNC_CALL_RBRACKET() { return getToken(PlotScriptParser.FUNC_CALL_RBRACKET, 0); }
		public List<TerminalNode> VALUE_DELIMITER() { return getTokens(PlotScriptParser.VALUE_DELIMITER); }
		public TerminalNode VALUE_DELIMITER(int i) {
			return getToken(PlotScriptParser.VALUE_DELIMITER, i);
		}
		public ExportStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exportStatement; }
	}

	public final ExportStatementContext exportStatement() throws RecognitionException {
		ExportStatementContext _localctx = new ExportStatementContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_exportStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			match(EXPORT);
			setState(266);
			match(FUNC_CALL_LBRACKET);
			setState(267);
			plotName();
			setState(272);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==VALUE_DELIMITER) {
				{
				{
				setState(268);
				match(VALUE_DELIMITER);
				setState(269);
				plotName();
				}
				}
				setState(274);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(275);
			match(FUNC_CALL_RBRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 25:
			return cpp_expr_sempred((Cpp_exprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean cpp_expr_sempred(Cpp_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 8);
		case 1:
			return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001N\u0116\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0001\u0000\u0005\u0000"+
		"8\b\u0000\n\u0000\f\u0000;\t\u0000\u0001\u0000\u0003\u0000>\b\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0003\u0001\u0003\u0005\u0003I\b\u0003\n\u0003\f\u0003L\t"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0003\u0006Z\b\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0005\bb\b\b\n\b\f\be\t\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0003\tx\b\t\u0001\n\u0001\n\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u0095\b\f\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0003\u000e\u009f\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u00a6\b\u000f\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0011\u0005\u0011\u00ad\b\u0011\n\u0011\f\u0011\u00b0"+
		"\t\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0005"+
		"\u0013\u00b7\b\u0013\n\u0013\f\u0013\u00ba\t\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u00c8\b\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0003\u0016\u00d2\b\u0016\u0001\u0017\u0001\u0017"+
		"\u0003\u0017\u00d6\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u00df\b\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0003\u0018\u00e4\b\u0018\u0001\u0018\u0001\u0018"+
		"\u0003\u0018\u00e8\b\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u00ec\b"+
		"\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u00f0\b\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u00fd\b\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0005"+
		"\u0019\u0105\b\u0019\n\u0019\f\u0019\u0108\t\u0019\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0005\u001a\u010f\b\u001a\n\u001a"+
		"\f\u001a\u0112\t\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0000\u0001"+
		"2\u001b\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018"+
		"\u001a\u001c\u001e \"$&(*,.024\u0000\u0005\u0002\u0000\n\r\u0010\u0011"+
		"\u0001\u0000\u0019\u001b\u0001\u0000\u001e \u0001\u0000\"%\u0001\u0000"+
		"12\u0119\u00009\u0001\u0000\u0000\u0000\u0002A\u0001\u0000\u0000\u0000"+
		"\u0004D\u0001\u0000\u0000\u0000\u0006F\u0001\u0000\u0000\u0000\bO\u0001"+
		"\u0000\u0000\u0000\nT\u0001\u0000\u0000\u0000\fY\u0001\u0000\u0000\u0000"+
		"\u000e[\u0001\u0000\u0000\u0000\u0010]\u0001\u0000\u0000\u0000\u0012w"+
		"\u0001\u0000\u0000\u0000\u0014y\u0001\u0000\u0000\u0000\u0016{\u0001\u0000"+
		"\u0000\u0000\u0018\u0094\u0001\u0000\u0000\u0000\u001a\u0096\u0001\u0000"+
		"\u0000\u0000\u001c\u009a\u0001\u0000\u0000\u0000\u001e\u00a5\u0001\u0000"+
		"\u0000\u0000 \u00a7\u0001\u0000\u0000\u0000\"\u00a9\u0001\u0000\u0000"+
		"\u0000$\u00b1\u0001\u0000\u0000\u0000&\u00b4\u0001\u0000\u0000\u0000("+
		"\u00c7\u0001\u0000\u0000\u0000*\u00c9\u0001\u0000\u0000\u0000,\u00cd\u0001"+
		"\u0000\u0000\u0000.\u00d3\u0001\u0000\u0000\u00000\u00ef\u0001\u0000\u0000"+
		"\u00002\u00fc\u0001\u0000\u0000\u00004\u0109\u0001\u0000\u0000\u00006"+
		"8\u0003\u0002\u0001\u000076\u0001\u0000\u0000\u00008;\u0001\u0000\u0000"+
		"\u000097\u0001\u0000\u0000\u00009:\u0001\u0000\u0000\u0000:=\u0001\u0000"+
		"\u0000\u0000;9\u0001\u0000\u0000\u0000<>\u00034\u001a\u0000=<\u0001\u0000"+
		"\u0000\u0000=>\u0001\u0000\u0000\u0000>?\u0001\u0000\u0000\u0000?@\u0005"+
		"\u0000\u0000\u0001@\u0001\u0001\u0000\u0000\u0000AB\u0003\u0004\u0002"+
		"\u0000BC\u0003\u0006\u0003\u0000C\u0003\u0001\u0000\u0000\u0000DE\u0005"+
		"\u001b\u0000\u0000E\u0005\u0001\u0000\u0000\u0000FJ\u0005\u0001\u0000"+
		"\u0000GI\u0003\b\u0004\u0000HG\u0001\u0000\u0000\u0000IL\u0001\u0000\u0000"+
		"\u0000JH\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KM\u0001\u0000"+
		"\u0000\u0000LJ\u0001\u0000\u0000\u0000MN\u0005\u0002\u0000\u0000N\u0007"+
		"\u0001\u0000\u0000\u0000OP\u0003\n\u0005\u0000PQ\u0005\t\u0000\u0000Q"+
		"R\u0003\f\u0006\u0000RS\u0005\u0007\u0000\u0000S\t\u0001\u0000\u0000\u0000"+
		"TU\u0007\u0000\u0000\u0000U\u000b\u0001\u0000\u0000\u0000VZ\u0003\u000e"+
		"\u0007\u0000WZ\u0003\u0010\b\u0000XZ\u0003\u0012\t\u0000YV\u0001\u0000"+
		"\u0000\u0000YW\u0001\u0000\u0000\u0000YX\u0001\u0000\u0000\u0000Z\r\u0001"+
		"\u0000\u0000\u0000[\\\u0007\u0001\u0000\u0000\\\u000f\u0001\u0000\u0000"+
		"\u0000]^\u0005\u0003\u0000\u0000^c\u0003\u000e\u0007\u0000_`\u0005\b\u0000"+
		"\u0000`b\u0003\u000e\u0007\u0000a_\u0001\u0000\u0000\u0000be\u0001\u0000"+
		"\u0000\u0000ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000df\u0001"+
		"\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000fg\u0005\u0004\u0000\u0000"+
		"g\u0011\u0001\u0000\u0000\u0000hi\u0005\u000e\u0000\u0000ij\u0005\u0005"+
		"\u0000\u0000jk\u0003\u0018\f\u0000kl\u0005\u0006\u0000\u0000lx\u0001\u0000"+
		"\u0000\u0000mn\u0003\u0014\n\u0000no\u0005\u0005\u0000\u0000op\u0003\u0016"+
		"\u000b\u0000pq\u0005\u0006\u0000\u0000qx\u0001\u0000\u0000\u0000rs\u0005"+
		"\u0012\u0000\u0000st\u0005\u0005\u0000\u0000tu\u0003\u001a\r\u0000uv\u0005"+
		"\u0006\u0000\u0000vx\u0001\u0000\u0000\u0000wh\u0001\u0000\u0000\u0000"+
		"wm\u0001\u0000\u0000\u0000wr\u0001\u0000\u0000\u0000x\u0013\u0001\u0000"+
		"\u0000\u0000yz\u0005\u000f\u0000\u0000z\u0015\u0001\u0000\u0000\u0000"+
		"{|\u0005\u0019\u0000\u0000|\u0017\u0001\u0000\u0000\u0000}~\u0005\u0013"+
		"\u0000\u0000~\u007f\u0005\u0005\u0000\u0000\u007f\u0080\u0005\u001a\u0000"+
		"\u0000\u0080\u0081\u0005\u0006\u0000\u0000\u0081\u0082\u0005\b\u0000\u0000"+
		"\u0082\u0083\u0005\u0014\u0000\u0000\u0083\u0084\u0005\u0005\u0000\u0000"+
		"\u0084\u0085\u0005\u001a\u0000\u0000\u0085\u0095\u0005\u0006\u0000\u0000"+
		"\u0086\u0087\u0005\u0013\u0000\u0000\u0087\u0088\u0005\u0005\u0000\u0000"+
		"\u0088\u0089\u0005\u001a\u0000\u0000\u0089\u008a\u0005\u0006\u0000\u0000"+
		"\u008a\u008b\u0005\b\u0000\u0000\u008b\u008c\u0005\u0014\u0000\u0000\u008c"+
		"\u008d\u0005\u0005\u0000\u0000\u008d\u008e\u0005\u001a\u0000\u0000\u008e"+
		"\u008f\u0005\u0006\u0000\u0000\u008f\u0090\u0005\b\u0000\u0000\u0090\u0091"+
		"\u0005\u0015\u0000\u0000\u0091\u0092\u0005\u0005\u0000\u0000\u0092\u0093"+
		"\u0005\u001a\u0000\u0000\u0093\u0095\u0005\u0006\u0000\u0000\u0094}\u0001"+
		"\u0000\u0000\u0000\u0094\u0086\u0001\u0000\u0000\u0000\u0095\u0019\u0001"+
		"\u0000\u0000\u0000\u0096\u0097\u0005\u0017\u0000\u0000\u0097\u0098\u0003"+
		"\u001c\u000e\u0000\u0098\u0099\u0005\u001d\u0000\u0000\u0099\u001b\u0001"+
		"\u0000\u0000\u0000\u009a\u009b\u0003\u001e\u000f\u0000\u009b\u009c\u0005"+
		"5\u0000\u0000\u009c\u009e\u0005)\u0000\u0000\u009d\u009f\u0003\"\u0011"+
		"\u0000\u009e\u009d\u0001\u0000\u0000\u0000\u009e\u009f\u0001\u0000\u0000"+
		"\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u00a1\u0005*\u0000\u0000"+
		"\u00a1\u00a2\u0003&\u0013\u0000\u00a2\u001d\u0001\u0000\u0000\u0000\u00a3"+
		"\u00a6\u0005!\u0000\u0000\u00a4\u00a6\u0003 \u0010\u0000\u00a5\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a6\u001f\u0001"+
		"\u0000\u0000\u0000\u00a7\u00a8\u0007\u0002\u0000\u0000\u00a8!\u0001\u0000"+
		"\u0000\u0000\u00a9\u00ae\u0003$\u0012\u0000\u00aa\u00ab\u0005\'\u0000"+
		"\u0000\u00ab\u00ad\u0003$\u0012\u0000\u00ac\u00aa\u0001\u0000\u0000\u0000"+
		"\u00ad\u00b0\u0001\u0000\u0000\u0000\u00ae\u00ac\u0001\u0000\u0000\u0000"+
		"\u00ae\u00af\u0001\u0000\u0000\u0000\u00af#\u0001\u0000\u0000\u0000\u00b0"+
		"\u00ae\u0001\u0000\u0000\u0000\u00b1\u00b2\u0003 \u0010\u0000\u00b2\u00b3"+
		"\u00055\u0000\u0000\u00b3%\u0001\u0000\u0000\u0000\u00b4\u00b8\u0005+"+
		"\u0000\u0000\u00b5\u00b7\u0003(\u0014\u0000\u00b6\u00b5\u0001\u0000\u0000"+
		"\u0000\u00b7\u00ba\u0001\u0000\u0000\u0000\u00b8\u00b6\u0001\u0000\u0000"+
		"\u0000\u00b8\u00b9\u0001\u0000\u0000\u0000\u00b9\u00bb\u0001\u0000\u0000"+
		"\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00bb\u00bc\u0005,\u0000\u0000"+
		"\u00bc\'\u0001\u0000\u0000\u0000\u00bd\u00be\u0003,\u0016\u0000\u00be"+
		"\u00bf\u0005(\u0000\u0000\u00bf\u00c8\u0001\u0000\u0000\u0000\u00c0\u00c1"+
		"\u0003*\u0015\u0000\u00c1\u00c2\u0005(\u0000\u0000\u00c2\u00c8\u0001\u0000"+
		"\u0000\u0000\u00c3\u00c4\u0003.\u0017\u0000\u00c4\u00c5\u0005(\u0000\u0000"+
		"\u00c5\u00c8\u0001\u0000\u0000\u0000\u00c6\u00c8\u00030\u0018\u0000\u00c7"+
		"\u00bd\u0001\u0000\u0000\u0000\u00c7\u00c0\u0001\u0000\u0000\u0000\u00c7"+
		"\u00c3\u0001\u0000\u0000\u0000\u00c7\u00c6\u0001\u0000\u0000\u0000\u00c8"+
		")\u0001\u0000\u0000\u0000\u00c9\u00ca\u00055\u0000\u0000\u00ca\u00cb\u0005"+
		"&\u0000\u0000\u00cb\u00cc\u00032\u0019\u0000\u00cc+\u0001\u0000\u0000"+
		"\u0000\u00cd\u00ce\u0003 \u0010\u0000\u00ce\u00d1\u00055\u0000\u0000\u00cf"+
		"\u00d0\u0005&\u0000\u0000\u00d0\u00d2\u00032\u0019\u0000\u00d1\u00cf\u0001"+
		"\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2-\u0001\u0000"+
		"\u0000\u0000\u00d3\u00d5\u00050\u0000\u0000\u00d4\u00d6\u00032\u0019\u0000"+
		"\u00d5\u00d4\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d6/\u0001\u0000\u0000\u0000\u00d7\u00d8\u0005-\u0000\u0000\u00d8\u00d9"+
		"\u0005)\u0000\u0000\u00d9\u00da\u00032\u0019\u0000\u00da\u00db\u0005*"+
		"\u0000\u0000\u00db\u00de\u0003&\u0013\u0000\u00dc\u00dd\u0005.\u0000\u0000"+
		"\u00dd\u00df\u0003&\u0013\u0000\u00de\u00dc\u0001\u0000\u0000\u0000\u00de"+
		"\u00df\u0001\u0000\u0000\u0000\u00df\u00f0\u0001\u0000\u0000\u0000\u00e0"+
		"\u00e1\u0005/\u0000\u0000\u00e1\u00e3\u0005)\u0000\u0000\u00e2\u00e4\u0003"+
		"*\u0015\u0000\u00e3\u00e2\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000"+
		"\u0000\u0000\u00e4\u00e5\u0001\u0000\u0000\u0000\u00e5\u00e7\u0005(\u0000"+
		"\u0000\u00e6\u00e8\u00032\u0019\u0000\u00e7\u00e6\u0001\u0000\u0000\u0000"+
		"\u00e7\u00e8\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000"+
		"\u00e9\u00eb\u0005(\u0000\u0000\u00ea\u00ec\u0003*\u0015\u0000\u00eb\u00ea"+
		"\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000\u0000\u0000\u00ec\u00ed"+
		"\u0001\u0000\u0000\u0000\u00ed\u00ee\u0005*\u0000\u0000\u00ee\u00f0\u0003"+
		"&\u0013\u0000\u00ef\u00d7\u0001\u0000\u0000\u0000\u00ef\u00e0\u0001\u0000"+
		"\u0000\u0000\u00f01\u0001\u0000\u0000\u0000\u00f1\u00f2\u0006\u0019\uffff"+
		"\uffff\u0000\u00f2\u00f3\u0005)\u0000\u0000\u00f3\u00f4\u00032\u0019\u0000"+
		"\u00f4\u00f5\u0005*\u0000\u0000\u00f5\u00fd\u0001\u0000\u0000\u0000\u00f6"+
		"\u00fd\u00055\u0000\u0000\u00f7\u00fd\u00054\u0000\u0000\u00f8\u00f9\u0005"+
		"3\u0000\u0000\u00f9\u00fd\u00032\u0019\u0003\u00fa\u00fd\u00056\u0000"+
		"\u0000\u00fb\u00fd\u00057\u0000\u0000\u00fc\u00f1\u0001\u0000\u0000\u0000"+
		"\u00fc\u00f6\u0001\u0000\u0000\u0000\u00fc\u00f7\u0001\u0000\u0000\u0000"+
		"\u00fc\u00f8\u0001\u0000\u0000\u0000\u00fc\u00fa\u0001\u0000\u0000\u0000"+
		"\u00fc\u00fb\u0001\u0000\u0000\u0000\u00fd\u0106\u0001\u0000\u0000\u0000"+
		"\u00fe\u00ff\n\b\u0000\u0000\u00ff\u0100\u0007\u0003\u0000\u0000\u0100"+
		"\u0105\u00032\u0019\t\u0101\u0102\n\u0004\u0000\u0000\u0102\u0103\u0007"+
		"\u0004\u0000\u0000\u0103\u0105\u00032\u0019\u0005\u0104\u00fe\u0001\u0000"+
		"\u0000\u0000\u0104\u0101\u0001\u0000\u0000\u0000\u0105\u0108\u0001\u0000"+
		"\u0000\u0000\u0106\u0104\u0001\u0000\u0000\u0000\u0106\u0107\u0001\u0000"+
		"\u0000\u0000\u01073\u0001\u0000\u0000\u0000\u0108\u0106\u0001\u0000\u0000"+
		"\u0000\u0109\u010a\u0005\u0016\u0000\u0000\u010a\u010b\u0005\u0005\u0000"+
		"\u0000\u010b\u0110\u0003\u0004\u0002\u0000\u010c\u010d\u0005\b\u0000\u0000"+
		"\u010d\u010f\u0003\u0004\u0002\u0000\u010e\u010c\u0001\u0000\u0000\u0000"+
		"\u010f\u0112\u0001\u0000\u0000\u0000\u0110\u010e\u0001\u0000\u0000\u0000"+
		"\u0110\u0111\u0001\u0000\u0000\u0000\u0111\u0113\u0001\u0000\u0000\u0000"+
		"\u0112\u0110\u0001\u0000\u0000\u0000\u0113\u0114\u0005\u0006\u0000\u0000"+
		"\u01145\u0001\u0000\u0000\u0000\u00179=JYcw\u0094\u009e\u00a5\u00ae\u00b8"+
		"\u00c7\u00d1\u00d5\u00de\u00e3\u00e7\u00eb\u00ef\u00fc\u0104\u0106\u0110";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}