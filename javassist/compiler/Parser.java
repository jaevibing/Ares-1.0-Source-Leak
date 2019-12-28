package javassist.compiler;

import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.StringL;
import javassist.compiler.ast.Variable;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.BinExpr;
import javassist.compiler.ast.InstanceOfExpr;
import javassist.compiler.ast.CondExpr;
import javassist.compiler.ast.AssignExpr;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.Pair;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.FieldDecl;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.ASTList;

public final class Parser implements TokenId
{
    private Lex lex;
    private static final int[] binaryOpPrecedence;
    
    public Parser(final Lex a1) {
        this.lex = a1;
    }
    
    public boolean hasMore() {
        /*SL:28*/return this.lex.lookAhead() >= 0;
    }
    
    public ASTList parseMember(final SymbolTable a1) throws CompileError {
        final ASTList v1 = /*EL:34*/this.parseMember1(a1);
        /*SL:35*/if (v1 instanceof MethodDecl) {
            /*SL:36*/return this.parseMethod2(a1, (MethodDecl)v1);
        }
        /*SL:38*/return v1;
    }
    
    public ASTList parseMember1(final SymbolTable v-3) throws CompileError {
        final ASTList memberMods = /*EL:44*/this.parseMemberMods();
        boolean v0 = /*EL:46*/false;
        final Declarator formalType;
        /*SL:47*/if (this.lex.lookAhead() == 400 && this.lex.lookAhead(1) == 40) {
            final Declarator a1 = /*EL:48*/new Declarator(344, 0);
            /*SL:49*/v0 = true;
        }
        else {
            /*SL:52*/formalType = this.parseFormalType(v-3);
        }
        /*SL:54*/if (this.lex.get() != 400) {
            /*SL:55*/throw new SyntaxError(this.lex);
        }
        String v;
        /*SL:58*/if (v0) {
            /*SL:59*/v = "<init>";
        }
        else {
            /*SL:61*/v = this.lex.getString();
        }
        /*SL:63*/formalType.setVariable(new Symbol(v));
        /*SL:64*/if (v0 || this.lex.lookAhead() == 40) {
            /*SL:65*/return this.parseMethod1(v-3, v0, memberMods, formalType);
        }
        /*SL:67*/return this.parseField(v-3, memberMods, formalType);
    }
    
    private FieldDecl parseField(final SymbolTable a1, final ASTList a2, final Declarator a3) throws CompileError {
        ASTree v1 = /*EL:78*/null;
        /*SL:79*/if (this.lex.lookAhead() == 61) {
            /*SL:80*/this.lex.get();
            /*SL:81*/v1 = this.parseExpression(a1);
        }
        final int v2 = /*EL:84*/this.lex.get();
        /*SL:85*/if (v2 == 59) {
            /*SL:86*/return new FieldDecl(a2, new ASTList(a3, new ASTList(v1)));
        }
        /*SL:87*/if (v2 == 44) {
            /*SL:88*/throw new CompileError("only one field can be declared in one declaration", this.lex);
        }
        /*SL:91*/throw new SyntaxError(this.lex);
    }
    
    private MethodDecl parseMethod1(final SymbolTable a3, final boolean a4, final ASTList v1, final Declarator v2) throws CompileError {
        /*SL:108*/if (this.lex.get() != 40) {
            /*SL:109*/throw new SyntaxError(this.lex);
        }
        ASTList v3 = /*EL:111*/null;
        /*SL:112*/if (this.lex.lookAhead() != 41) {
            while (true) {
                /*SL:114*/v3 = ASTList.append(v3, this.parseFormalParam(a3));
                final int a5 = /*EL:115*/this.lex.lookAhead();
                /*SL:116*/if (a5 == 44) {
                    /*SL:117*/this.lex.get();
                }
                else {
                    /*SL:118*/if (a5 == 41) {
                        break;
                    }
                    continue;
                }
            }
        }
        /*SL:122*/this.lex.get();
        /*SL:123*/v2.addArrayDim(this.parseArrayDimension());
        /*SL:124*/if (a4 && v2.getArrayDim() > 0) {
            /*SL:125*/throw new SyntaxError(this.lex);
        }
        ASTList v4 = /*EL:127*/null;
        /*SL:128*/if (this.lex.lookAhead() == 341) {
            /*SL:129*/this.lex.get();
            while (true) {
                /*SL:131*/v4 = ASTList.append(v4, this.parseClassType(a3));
                /*SL:132*/if (this.lex.lookAhead() != 44) {
                    break;
                }
                /*SL:133*/this.lex.get();
            }
        }
        /*SL:140*/return new MethodDecl(v1, new ASTList(v2, ASTList.make(v3, v4, null)));
    }
    
    public MethodDecl parseMethod2(final SymbolTable a1, final MethodDecl a2) throws CompileError {
        Stmnt v1 = /*EL:148*/null;
        /*SL:149*/if (this.lex.lookAhead() == 59) {
            /*SL:150*/this.lex.get();
        }
        else {
            /*SL:152*/v1 = this.parseBlock(a1);
            /*SL:153*/if (v1 == null) {
                /*SL:154*/v1 = new Stmnt(66);
            }
        }
        /*SL:157*/a2.sublist(4).setHead(v1);
        /*SL:158*/return a2;
    }
    
    private ASTList parseMemberMods() {
        ASTList v2 = /*EL:168*/null;
        while (true) {
            /*SL:170*/v2 = this.lex.lookAhead();
            /*SL:171*/if (v2 != 300 && v2 != 315 && v2 != 332 && v2 != 331 && v2 != 330 && v2 != 338 && v2 != 335 && v2 != 345 && v2 != 342 && v2 != 347) {
                break;
            }
            /*SL:174*/v2 = new ASTList(new Keyword(this.lex.get()), v2);
        }
        /*SL:179*/return v2;
    }
    
    private Declarator parseFormalType(final SymbolTable v-1) throws CompileError {
        final int v0 = /*EL:185*/this.lex.lookAhead();
        /*SL:186*/if (isBuiltinType(v0) || v0 == 344) {
            /*SL:187*/this.lex.get();
            final int a1 = /*EL:188*/this.parseArrayDimension();
            /*SL:189*/return new Declarator(v0, a1);
        }
        final ASTList v = /*EL:192*/this.parseClassType(v-1);
        final int v2 = /*EL:193*/this.parseArrayDimension();
        /*SL:194*/return new Declarator(v, v2);
    }
    
    private static boolean isBuiltinType(final int a1) {
        /*SL:199*/return a1 == 301 || a1 == 303 || a1 == 306 || a1 == 334 || a1 == 324 || a1 == 326 || a1 == 317 || a1 == 312;
    }
    
    private Declarator parseFormalParam(final SymbolTable a1) throws CompileError {
        final Declarator v1 = /*EL:208*/this.parseFormalType(a1);
        /*SL:209*/if (this.lex.get() != 400) {
            /*SL:210*/throw new SyntaxError(this.lex);
        }
        final String v2 = /*EL:212*/this.lex.getString();
        /*SL:213*/v1.setVariable(new Symbol(v2));
        /*SL:214*/v1.addArrayDim(this.parseArrayDimension());
        /*SL:215*/a1.append(v2, v1);
        /*SL:216*/return v1;
    }
    
    public Stmnt parseStatement(final SymbolTable v2) throws CompileError {
        final int v3 = /*EL:241*/this.lex.lookAhead();
        /*SL:242*/if (v3 == 123) {
            /*SL:243*/return this.parseBlock(v2);
        }
        /*SL:244*/if (v3 == 59) {
            /*SL:245*/this.lex.get();
            /*SL:246*/return new Stmnt(66);
        }
        /*SL:248*/if (v3 == 400 && this.lex.lookAhead(1) == 58) {
            /*SL:249*/this.lex.get();
            final String a1 = /*EL:250*/this.lex.getString();
            /*SL:251*/this.lex.get();
            /*SL:252*/return Stmnt.make(76, new Symbol(a1), this.parseStatement(v2));
        }
        /*SL:254*/if (v3 == 320) {
            /*SL:255*/return this.parseIf(v2);
        }
        /*SL:256*/if (v3 == 346) {
            /*SL:257*/return this.parseWhile(v2);
        }
        /*SL:258*/if (v3 == 311) {
            /*SL:259*/return this.parseDo(v2);
        }
        /*SL:260*/if (v3 == 318) {
            /*SL:261*/return this.parseFor(v2);
        }
        /*SL:262*/if (v3 == 343) {
            /*SL:263*/return this.parseTry(v2);
        }
        /*SL:264*/if (v3 == 337) {
            /*SL:265*/return this.parseSwitch(v2);
        }
        /*SL:266*/if (v3 == 338) {
            /*SL:267*/return this.parseSynchronized(v2);
        }
        /*SL:268*/if (v3 == 333) {
            /*SL:269*/return this.parseReturn(v2);
        }
        /*SL:270*/if (v3 == 340) {
            /*SL:271*/return this.parseThrow(v2);
        }
        /*SL:272*/if (v3 == 302) {
            /*SL:273*/return this.parseBreak(v2);
        }
        /*SL:274*/if (v3 == 309) {
            /*SL:275*/return this.parseContinue(v2);
        }
        /*SL:277*/return this.parseDeclarationOrExpression(v2, false);
    }
    
    private Stmnt parseBlock(final SymbolTable v2) throws CompileError {
        /*SL:283*/if (this.lex.get() != 123) {
            /*SL:284*/throw new SyntaxError(this.lex);
        }
        Stmnt v3 = /*EL:286*/null;
        final SymbolTable v4 = /*EL:287*/new SymbolTable(v2);
        /*SL:288*/while (this.lex.lookAhead() != 125) {
            final Stmnt a1 = /*EL:289*/this.parseStatement(v4);
            /*SL:290*/if (a1 != null) {
                /*SL:291*/v3 = (Stmnt)ASTList.concat(v3, new Stmnt(66, a1));
            }
        }
        /*SL:294*/this.lex.get();
        /*SL:295*/if (v3 == null) {
            /*SL:296*/return new Stmnt(66);
        }
        /*SL:298*/return v3;
    }
    
    private Stmnt parseIf(final SymbolTable v2) throws CompileError {
        final int v3 = /*EL:305*/this.lex.get();
        final ASTree v4 = /*EL:306*/this.parseParExpression(v2);
        final Stmnt v5 = /*EL:307*/this.parseStatement(v2);
        final Stmnt v6;
        /*SL:309*/if (this.lex.lookAhead() == 313) {
            /*SL:310*/this.lex.get();
            final Stmnt a1 = /*EL:311*/this.parseStatement(v2);
        }
        else {
            /*SL:314*/v6 = null;
        }
        /*SL:316*/return new Stmnt(v3, v4, new ASTList(v5, new ASTList(v6)));
    }
    
    private Stmnt parseWhile(final SymbolTable a1) throws CompileError {
        final int v1 = /*EL:324*/this.lex.get();
        final ASTree v2 = /*EL:325*/this.parseParExpression(a1);
        final Stmnt v3 = /*EL:326*/this.parseStatement(a1);
        /*SL:327*/return new Stmnt(v1, v2, v3);
    }
    
    private Stmnt parseDo(final SymbolTable a1) throws CompileError {
        final int v1 = /*EL:333*/this.lex.get();
        final Stmnt v2 = /*EL:334*/this.parseStatement(a1);
        /*SL:335*/if (this.lex.get() != 346 || this.lex.get() != 40) {
            /*SL:336*/throw new SyntaxError(this.lex);
        }
        final ASTree v3 = /*EL:338*/this.parseExpression(a1);
        /*SL:339*/if (this.lex.get() != 41 || this.lex.get() != 59) {
            /*SL:340*/throw new SyntaxError(this.lex);
        }
        /*SL:342*/return new Stmnt(v1, v3, v2);
    }
    
    private Stmnt parseFor(final SymbolTable v-2) throws CompileError {
        int v2 = /*EL:351*/this.lex.get();
        /*SL:353*/v2 = new SymbolTable(v-2);
        /*SL:355*/if (this.lex.get() != 40) {
            /*SL:356*/throw new SyntaxError(this.lex);
        }
        final Stmnt declarationOrExpression;
        /*SL:358*/if (this.lex.lookAhead() == 59) {
            /*SL:359*/this.lex.get();
            final Stmnt a1 = /*EL:360*/null;
        }
        else {
            /*SL:363*/declarationOrExpression = this.parseDeclarationOrExpression(v2, true);
        }
        ASTree v3;
        /*SL:365*/if (this.lex.lookAhead() == 59) {
            /*SL:366*/v3 = null;
        }
        else {
            /*SL:368*/v3 = this.parseExpression(v2);
        }
        /*SL:370*/if (this.lex.get() != 59) {
            /*SL:371*/throw new CompileError("; is missing", this.lex);
        }
        Stmnt v4;
        /*SL:373*/if (this.lex.lookAhead() == 41) {
            /*SL:374*/v4 = null;
        }
        else {
            /*SL:376*/v4 = this.parseExprList(v2);
        }
        /*SL:378*/if (this.lex.get() != 41) {
            /*SL:379*/throw new CompileError(") is missing", this.lex);
        }
        final Stmnt v5 = /*EL:381*/this.parseStatement(v2);
        /*SL:382*/return new Stmnt(v2, declarationOrExpression, new ASTList(v3, new ASTList(v4, v5)));
    }
    
    private Stmnt parseSwitch(final SymbolTable a1) throws CompileError {
        final int v1 = /*EL:394*/this.lex.get();
        final ASTree v2 = /*EL:395*/this.parseParExpression(a1);
        final Stmnt v3 = /*EL:396*/this.parseSwitchBlock(a1);
        /*SL:397*/return new Stmnt(v1, v2, v3);
    }
    
    private Stmnt parseSwitchBlock(final SymbolTable v-4) throws CompileError {
        /*SL:401*/if (this.lex.get() != 123) {
            /*SL:402*/throw new SyntaxError(this.lex);
        }
        final SymbolTable symbolTable = /*EL:404*/new SymbolTable(v-4);
        Stmnt stmntOrCase = /*EL:405*/this.parseStmntOrCase(symbolTable);
        /*SL:406*/if (stmntOrCase == null) {
            /*SL:407*/throw new CompileError("empty switch block", this.lex);
        }
        final int operator = /*EL:409*/stmntOrCase.getOperator();
        /*SL:410*/if (operator != 304 && operator != 310) {
            /*SL:411*/throw new CompileError("no case or default in a switch block", this.lex);
        }
        Stmnt v0 = /*EL:414*/new Stmnt(66, stmntOrCase);
        /*SL:415*/while (this.lex.lookAhead() != 125) {
            final Stmnt v = /*EL:416*/this.parseStmntOrCase(symbolTable);
            /*SL:417*/if (v != null) {
                final int a1 = /*EL:418*/v.getOperator();
                /*SL:419*/if (a1 == 304 || a1 == 310) {
                    /*SL:420*/v0 = (Stmnt)ASTList.concat(v0, new Stmnt(66, v));
                    /*SL:421*/stmntOrCase = v;
                }
                else {
                    /*SL:424*/stmntOrCase = (Stmnt)ASTList.concat(stmntOrCase, new Stmnt(66, v));
                }
            }
        }
        /*SL:428*/this.lex.get();
        /*SL:429*/return v0;
    }
    
    private Stmnt parseStmntOrCase(final SymbolTable v2) throws CompileError {
        final int v3 = /*EL:433*/this.lex.lookAhead();
        /*SL:434*/if (v3 != 304 && v3 != 310) {
            /*SL:435*/return this.parseStatement(v2);
        }
        /*SL:437*/this.lex.get();
        final Stmnt v4;
        /*SL:439*/if (v3 == 304) {
            final Stmnt a1 = /*EL:440*/new Stmnt(v3, this.parseExpression(v2));
        }
        else {
            /*SL:442*/v4 = new Stmnt(310);
        }
        /*SL:444*/if (this.lex.get() != 58) {
            /*SL:445*/throw new CompileError(": is missing", this.lex);
        }
        /*SL:447*/return v4;
    }
    
    private Stmnt parseSynchronized(final SymbolTable a1) throws CompileError {
        final int v1 = /*EL:454*/this.lex.get();
        /*SL:455*/if (this.lex.get() != 40) {
            /*SL:456*/throw new SyntaxError(this.lex);
        }
        final ASTree v2 = /*EL:458*/this.parseExpression(a1);
        /*SL:459*/if (this.lex.get() != 41) {
            /*SL:460*/throw new SyntaxError(this.lex);
        }
        final Stmnt v3 = /*EL:462*/this.parseBlock(a1);
        /*SL:463*/return new Stmnt(v1, v2, v3);
    }
    
    private Stmnt parseTry(final SymbolTable v-3) throws CompileError {
        /*SL:472*/this.lex.get();
        final Stmnt block = /*EL:473*/this.parseBlock(v-3);
        ASTList append = /*EL:474*/null;
        /*SL:475*/while (this.lex.lookAhead() == 305) {
            /*SL:476*/this.lex.get();
            /*SL:477*/if (this.lex.get() != 40) {
                /*SL:478*/throw new SyntaxError(this.lex);
            }
            final SymbolTable a1 = /*EL:480*/new SymbolTable(v-3);
            final Declarator v1 = /*EL:481*/this.parseFormalParam(a1);
            /*SL:482*/if (v1.getArrayDim() > 0 || v1.getType() != 307) {
                /*SL:483*/throw new SyntaxError(this.lex);
            }
            /*SL:485*/if (this.lex.get() != 41) {
                /*SL:486*/throw new SyntaxError(this.lex);
            }
            final Stmnt v2 = /*EL:488*/this.parseBlock(a1);
            /*SL:489*/append = ASTList.append(append, new Pair(v1, v2));
        }
        Stmnt v3 = /*EL:492*/null;
        /*SL:493*/if (this.lex.lookAhead() == 316) {
            /*SL:494*/this.lex.get();
            /*SL:495*/v3 = this.parseBlock(v-3);
        }
        /*SL:498*/return Stmnt.make(343, block, append, v3);
    }
    
    private Stmnt parseReturn(final SymbolTable a1) throws CompileError {
        final int v1 = /*EL:504*/this.lex.get();
        final Stmnt v2 = /*EL:505*/new Stmnt(v1);
        /*SL:506*/if (this.lex.lookAhead() != 59) {
            /*SL:507*/v2.setLeft(this.parseExpression(a1));
        }
        /*SL:509*/if (this.lex.get() != 59) {
            /*SL:510*/throw new CompileError("; is missing", this.lex);
        }
        /*SL:512*/return v2;
    }
    
    private Stmnt parseThrow(final SymbolTable a1) throws CompileError {
        final int v1 = /*EL:518*/this.lex.get();
        final ASTree v2 = /*EL:519*/this.parseExpression(a1);
        /*SL:520*/if (this.lex.get() != 59) {
            /*SL:521*/throw new CompileError("; is missing", this.lex);
        }
        /*SL:523*/return new Stmnt(v1, v2);
    }
    
    private Stmnt parseBreak(final SymbolTable a1) throws CompileError {
        /*SL:531*/return this.parseContinue(a1);
    }
    
    private Stmnt parseContinue(final SymbolTable a1) throws CompileError {
        final int v1 = /*EL:539*/this.lex.get();
        final Stmnt v2 = /*EL:540*/new Stmnt(v1);
        int v3 = /*EL:541*/this.lex.get();
        /*SL:542*/if (v3 == 400) {
            /*SL:543*/v2.setLeft(new Symbol(this.lex.getString()));
            /*SL:544*/v3 = this.lex.get();
        }
        /*SL:547*/if (v3 != 59) {
            /*SL:548*/throw new CompileError("; is missing", this.lex);
        }
        /*SL:550*/return v2;
    }
    
    private Stmnt parseDeclarationOrExpression(final SymbolTable v-4, final boolean v-3) throws CompileError {
        int i;
        /*SL:567*/for (i = this.lex.lookAhead(); i == 315; /*SL:569*/i = this.lex.lookAhead()) {
            this.lex.get();
        }
        /*SL:572*/if (isBuiltinType(i)) {
            /*SL:573*/i = this.lex.get();
            final int a1 = /*EL:574*/this.parseArrayDimension();
            /*SL:575*/return this.parseDeclarators(v-4, new Declarator(i, a1));
        }
        /*SL:577*/if (i == 400) {
            final int nextIsClassType = /*EL:578*/this.nextIsClassType(0);
            /*SL:579*/if (nextIsClassType >= 0 && /*EL:580*/this.lex.lookAhead(nextIsClassType) == 400) {
                final ASTList a2 = /*EL:581*/this.parseClassType(v-4);
                final int v1 = /*EL:582*/this.parseArrayDimension();
                /*SL:583*/return this.parseDeclarators(v-4, new Declarator(a2, v1));
            }
        }
        Stmnt exprList;
        /*SL:588*/if (v-3) {
            /*SL:589*/exprList = this.parseExprList(v-4);
        }
        else {
            /*SL:591*/exprList = new Stmnt(69, this.parseExpression(v-4));
        }
        /*SL:593*/if (this.lex.get() != 59) {
            /*SL:594*/throw new CompileError("; is missing", this.lex);
        }
        /*SL:596*/return exprList;
    }
    
    private Stmnt parseExprList(final SymbolTable v2) throws CompileError {
        Stmnt v3 = /*EL:602*/null;
        while (true) {
            final Stmnt a1 = /*EL:604*/new Stmnt(69, this.parseExpression(v2));
            /*SL:605*/v3 = (Stmnt)ASTList.concat(v3, new Stmnt(66, a1));
            /*SL:606*/if (this.lex.lookAhead() != 44) {
                break;
            }
            /*SL:607*/this.lex.get();
        }
        /*SL:609*/return v3;
    }
    
    private Stmnt parseDeclarators(final SymbolTable v1, final Declarator v2) throws CompileError {
        Stmnt v3 = /*EL:618*/null;
        while (true) {
            /*SL:620*/v3 = (Stmnt)ASTList.concat(v3, /*EL:621*/new Stmnt(68, this.parseDeclarator(v1, v2)));
            final int a1 = /*EL:622*/this.lex.get();
            /*SL:623*/if (a1 == 59) {
                /*SL:624*/return v3;
            }
            /*SL:625*/if (a1 != 44) {
                /*SL:626*/throw new CompileError("; is missing", this.lex);
            }
        }
    }
    
    private Declarator parseDeclarator(final SymbolTable a1, final Declarator a2) throws CompileError {
        /*SL:635*/if (this.lex.get() != 400 || a2.getType() == 344) {
            /*SL:636*/throw new SyntaxError(this.lex);
        }
        final String v1 = /*EL:638*/this.lex.getString();
        final Symbol v2 = /*EL:639*/new Symbol(v1);
        final int v3 = /*EL:640*/this.parseArrayDimension();
        ASTree v4 = /*EL:641*/null;
        /*SL:642*/if (this.lex.lookAhead() == 61) {
            /*SL:643*/this.lex.get();
            /*SL:644*/v4 = this.parseInitializer(a1);
        }
        final Declarator v5 = /*EL:647*/a2.make(v2, v3, v4);
        /*SL:648*/a1.append(v1, v5);
        /*SL:649*/return v5;
    }
    
    private ASTree parseInitializer(final SymbolTable a1) throws CompileError {
        /*SL:655*/if (this.lex.lookAhead() == 123) {
            /*SL:656*/return this.parseArrayInitializer(a1);
        }
        /*SL:658*/return this.parseExpression(a1);
    }
    
    private ArrayInit parseArrayInitializer(final SymbolTable a1) throws CompileError {
        /*SL:667*/this.lex.get();
        ASTree v1 = /*EL:668*/this.parseExpression(a1);
        final ArrayInit v2 = /*EL:669*/new ArrayInit(v1);
        /*SL:670*/while (this.lex.lookAhead() == 44) {
            /*SL:671*/this.lex.get();
            /*SL:672*/v1 = this.parseExpression(a1);
            /*SL:673*/ASTList.append(v2, v1);
        }
        /*SL:676*/if (this.lex.get() != 125) {
            /*SL:677*/throw new SyntaxError(this.lex);
        }
        /*SL:679*/return v2;
    }
    
    private ASTree parseParExpression(final SymbolTable a1) throws CompileError {
        /*SL:685*/if (this.lex.get() != 40) {
            /*SL:686*/throw new SyntaxError(this.lex);
        }
        final ASTree v1 = /*EL:688*/this.parseExpression(a1);
        /*SL:689*/if (this.lex.get() != 41) {
            /*SL:690*/throw new SyntaxError(this.lex);
        }
        /*SL:692*/return v1;
    }
    
    public ASTree parseExpression(final SymbolTable a1) throws CompileError {
        final ASTree v1 = /*EL:699*/this.parseConditionalExpr(a1);
        /*SL:700*/if (!isAssignOp(this.lex.lookAhead())) {
            /*SL:701*/return v1;
        }
        final int v2 = /*EL:703*/this.lex.get();
        final ASTree v3 = /*EL:704*/this.parseExpression(a1);
        /*SL:705*/return AssignExpr.makeAssign(v2, v1, v3);
    }
    
    private static boolean isAssignOp(final int a1) {
        /*SL:709*/return a1 == 61 || a1 == 351 || a1 == 352 || a1 == 353 || a1 == 354 || a1 == 355 || a1 == 356 || a1 == 360 || a1 == 361 || a1 == 365 || a1 == 367 || a1 == 371;
    }
    
    private ASTree parseConditionalExpr(final SymbolTable v-2) throws CompileError {
        final ASTree binaryExpr = /*EL:719*/this.parseBinaryExpr(v-2);
        /*SL:720*/if (this.lex.lookAhead() != 63) {
            /*SL:730*/return binaryExpr;
        }
        this.lex.get();
        final ASTree a1 = this.parseExpression(v-2);
        if (this.lex.get() != 58) {
            throw new CompileError(": is missing", this.lex);
        }
        final ASTree v1 = this.parseExpression(v-2);
        return new CondExpr(binaryExpr, a1, v1);
    }
    
    private ASTree parseBinaryExpr(final SymbolTable v-2) throws CompileError {
        ASTree v2 = /*EL:775*/this.parseUnaryExpr(v-2);
        while (true) {
            final int a1 = /*EL:777*/this.lex.lookAhead();
            final int v1 = /*EL:778*/this.getOpPrecedence(a1);
            /*SL:779*/if (v1 == 0) {
                break;
            }
            /*SL:782*/v2 = this.binaryExpr2(v-2, v2, v1);
        }
        return v2;
    }
    
    private ASTree parseInstanceOf(final SymbolTable v-3, final ASTree v-2) throws CompileError {
        final int lookAhead = /*EL:789*/this.lex.lookAhead();
        /*SL:790*/if (isBuiltinType(lookAhead)) {
            /*SL:791*/this.lex.get();
            final int a1 = /*EL:792*/this.parseArrayDimension();
            /*SL:793*/return new InstanceOfExpr(lookAhead, a1, v-2);
        }
        final ASTList a2 = /*EL:796*/this.parseClassType(v-3);
        final int v1 = /*EL:797*/this.parseArrayDimension();
        /*SL:798*/return new InstanceOfExpr(a2, v1, v-2);
    }
    
    private ASTree binaryExpr2(final SymbolTable v1, final ASTree v2, final int v3) throws CompileError {
        final int v4 = /*EL:805*/this.lex.get();
        /*SL:806*/if (v4 == 323) {
            /*SL:807*/return this.parseInstanceOf(v1, v2);
        }
        ASTree v5 = /*EL:809*/this.parseUnaryExpr(v1);
        while (true) {
            final int a1 = /*EL:811*/this.lex.lookAhead();
            final int a2 = /*EL:812*/this.getOpPrecedence(a1);
            /*SL:813*/if (a2 == 0 || v3 <= a2) {
                break;
            }
            /*SL:814*/v5 = this.binaryExpr2(v1, v5, a2);
        }
        /*SL:816*/return BinExpr.makeBin(v4, v2, v5);
    }
    
    private int getOpPrecedence(final int a1) {
        /*SL:828*/if (33 <= a1 && a1 <= 63) {
            /*SL:829*/return Parser.binaryOpPrecedence[a1 - 33];
        }
        /*SL:830*/if (a1 == 94) {
            /*SL:831*/return 7;
        }
        /*SL:832*/if (a1 == 124) {
            /*SL:833*/return 8;
        }
        /*SL:834*/if (a1 == 369) {
            /*SL:835*/return 9;
        }
        /*SL:836*/if (a1 == 368) {
            /*SL:837*/return 10;
        }
        /*SL:838*/if (a1 == 358 || a1 == 350) {
            /*SL:839*/return 5;
        }
        /*SL:840*/if (a1 == 357 || a1 == 359 || a1 == 323) {
            /*SL:841*/return 4;
        }
        /*SL:842*/if (a1 == 364 || a1 == 366 || a1 == 370) {
            /*SL:843*/return 3;
        }
        /*SL:845*/return 0;
    }
    
    private ASTree parseUnaryExpr(final SymbolTable v0) throws CompileError {
        /*SL:859*/switch (this.lex.lookAhead()) {
            case 33:
            case 43:
            case 45:
            case 126:
            case 362:
            case 363: {
                final int v = /*EL:866*/this.lex.get();
                /*SL:867*/if (v == 45) {
                    final int a1 = /*EL:868*/this.lex.lookAhead();
                    /*SL:869*/switch (a1) {
                        case 401:
                        case 402:
                        case 403: {
                            /*SL:873*/this.lex.get();
                            /*SL:874*/return new IntConst(-this.lex.getLong(), a1);
                        }
                        case 404:
                        case 405: {
                            /*SL:877*/this.lex.get();
                            /*SL:878*/return new DoubleConst(-this.lex.getDouble(), a1);
                        }
                    }
                }
                /*SL:884*/return Expr.make(v, this.parseUnaryExpr(v0));
            }
            case 40: {
                /*SL:886*/return this.parseCast(v0);
            }
            default: {
                /*SL:888*/return this.parsePostfix(v0);
            }
        }
    }
    
    private ASTree parseCast(final SymbolTable v-1) throws CompileError {
        final int v0 = /*EL:901*/this.lex.lookAhead(1);
        /*SL:902*/if (isBuiltinType(v0) && this.nextIsBuiltinCast()) {
            /*SL:903*/this.lex.get();
            /*SL:904*/this.lex.get();
            final int a1 = /*EL:905*/this.parseArrayDimension();
            /*SL:906*/if (this.lex.get() != 41) {
                /*SL:907*/throw new CompileError(") is missing", this.lex);
            }
            /*SL:909*/return new CastExpr(v0, a1, this.parseUnaryExpr(v-1));
        }
        else {
            /*SL:911*/if (v0 != 400 || !this.nextIsClassCast()) {
                /*SL:921*/return this.parsePostfix(v-1);
            }
            this.lex.get();
            final ASTList v = this.parseClassType(v-1);
            final int v2 = this.parseArrayDimension();
            if (this.lex.get() != 41) {
                throw new CompileError(") is missing", this.lex);
            }
            return new CastExpr(v, v2, this.parseUnaryExpr(v-1));
        }
    }
    
    private boolean nextIsBuiltinCast() {
        int v2 = /*EL:926*/2;
        /*SL:927*/while ((v2 = this.lex.lookAhead(v2++)) == 91) {
            /*SL:928*/if (this.lex.lookAhead(v2++) != 93) {
                /*SL:929*/return false;
            }
        }
        /*SL:931*/return this.lex.lookAhead(v2 - 1) == 41;
    }
    
    private boolean nextIsClassCast() {
        final int v1 = /*EL:935*/this.nextIsClassType(1);
        /*SL:936*/if (v1 < 0) {
            /*SL:937*/return false;
        }
        int v2 = /*EL:939*/this.lex.lookAhead(v1);
        /*SL:940*/if (v2 != 41) {
            /*SL:941*/return false;
        }
        /*SL:943*/v2 = this.lex.lookAhead(v1 + 1);
        /*SL:944*/return v2 == 40 || v2 == 412 || v2 == 406 || v2 == 400 || v2 == 339 || v2 == 336 || v2 == 328 || v2 == 410 || v2 == 411 || v2 == 403 || v2 == 402 || v2 == 401 || v2 == 405 || v2 == 404;
    }
    
    private int nextIsClassType(int a1) {
        /*SL:953*/while (this.lex.lookAhead(++a1) == 46) {
            /*SL:954*/if (this.lex.lookAhead(++a1) != 400) {
                /*SL:955*/return -1;
            }
        }
        int v1;
        /*SL:957*/while ((v1 = this.lex.lookAhead(a1++)) == 91) {
            /*SL:958*/if (this.lex.lookAhead(a1++) != 93) {
                /*SL:959*/return -1;
            }
        }
        /*SL:961*/return a1 - 1;
    }
    
    private int parseArrayDimension() throws CompileError {
        int v1 = /*EL:967*/0;
        /*SL:968*/while (this.lex.lookAhead() == 91) {
            /*SL:969*/++v1;
            /*SL:970*/this.lex.get();
            /*SL:971*/if (this.lex.get() != 93) {
                /*SL:972*/throw new CompileError("] is missing", this.lex);
            }
        }
        /*SL:975*/return v1;
    }
    
    private ASTList parseClassType(final SymbolTable a1) throws CompileError {
        ASTList v1 = /*EL:981*/null;
        /*SL:983*/while (this.lex.get() == 400) {
            /*SL:986*/v1 = ASTList.append(v1, new Symbol(this.lex.getString()));
            /*SL:987*/if (this.lex.lookAhead() != 46) {
                /*SL:993*/return v1;
            }
            this.lex.get();
        }
        throw new SyntaxError(this.lex);
    }
    
    private ASTree parsePostfix(final SymbolTable v-2) throws CompileError {
        final int lookAhead = /*EL:1014*/this.lex.lookAhead();
        /*SL:1015*/switch (lookAhead) {
            case 401:
            case 402:
            case 403: {
                /*SL:1019*/this.lex.get();
                /*SL:1020*/return new IntConst(this.lex.getLong(), lookAhead);
            }
            case 404:
            case 405: {
                /*SL:1023*/this.lex.get();
                /*SL:1024*/return new DoubleConst(this.lex.getDouble(), lookAhead);
            }
            default: {
                ASTree v2 = /*EL:1031*/this.parsePrimaryExpr(v-2);
                while (true) {
                    /*SL:1034*/switch (this.lex.lookAhead()) {
                        case 40: {
                            /*SL:1036*/v2 = this.parseMethodCall(v-2, v2);
                            /*SL:1037*/continue;
                        }
                        case 91: {
                            /*SL:1039*/if (this.lex.lookAhead(1) == 93) {
                                final int a1 = /*EL:1040*/this.parseArrayDimension();
                                /*SL:1041*/if (this.lex.get() != 46 || this.lex.get() != 307) {
                                    /*SL:1042*/throw new SyntaxError(this.lex);
                                }
                                /*SL:1044*/v2 = this.parseDotClass(v2, a1);
                                /*SL:1045*/continue;
                            }
                            else {
                                /*SL:1047*/v2 = this.parseArrayIndex(v-2);
                                /*SL:1048*/if (v2 == null) {
                                    /*SL:1049*/throw new SyntaxError(this.lex);
                                }
                                /*SL:1051*/v2 = Expr.make(65, v2, v2);
                                /*SL:1053*/continue;
                            }
                            break;
                        }
                        case 362:
                        case 363: {
                            final int v3 = /*EL:1056*/this.lex.get();
                            /*SL:1057*/v2 = Expr.make(v3, null, v2);
                            /*SL:1058*/continue;
                        }
                        case 46: {
                            /*SL:1060*/this.lex.get();
                            final int v3 = /*EL:1061*/this.lex.get();
                            /*SL:1062*/if (v3 == 307) {
                                /*SL:1063*/v2 = this.parseDotClass(v2, 0);
                                continue;
                            }
                            /*SL:1064*/if (v3 == 336) {
                                /*SL:1065*/v2 = Expr.make(46, new Symbol(this.toClassName(v2)), new Keyword(v3));
                                continue;
                            }
                            /*SL:1066*/if (v3 == 400) {
                                final String v4 = /*EL:1067*/this.lex.getString();
                                /*SL:1068*/v2 = Expr.make(46, v2, new Member(v4));
                                continue;
                            }
                            /*SL:1071*/throw new CompileError("missing member name", this.lex);
                        }
                        case 35: {
                            /*SL:1074*/this.lex.get();
                            final int v3 = /*EL:1075*/this.lex.get();
                            /*SL:1076*/if (v3 != 400) {
                                /*SL:1077*/throw new CompileError("missing static member name", this.lex);
                            }
                            final String v4 = /*EL:1079*/this.lex.getString();
                            /*SL:1080*/v2 = Expr.make(35, new Symbol(this.toClassName(v2)), new Member(v4));
                            /*SL:1082*/continue;
                        }
                        default: {
                            /*SL:1084*/return v2;
                        }
                    }
                }
                break;
            }
        }
    }
    
    private ASTree parseDotClass(final ASTree v1, int v2) throws CompileError {
        String v3 = /*EL:1096*/this.toClassName(v1);
        /*SL:1097*/if (v2 > 0) {
            final StringBuffer a1 = /*EL:1098*/new StringBuffer();
            /*SL:1099*/while (v2-- > 0) {
                /*SL:1100*/a1.append('[');
            }
            /*SL:1102*/a1.append('L').append(v3.replace('.', '/')).append(';');
            /*SL:1103*/v3 = a1.toString();
        }
        /*SL:1106*/return Expr.make(46, new Symbol(v3), new Member("class"));
    }
    
    private ASTree parseDotClass(final int v-1, final int v0) throws CompileError {
        /*SL:1116*/if (v0 > 0) {
            final String a1 = /*EL:1117*/CodeGen.toJvmTypeName(v-1, v0);
            /*SL:1118*/return Expr.make(46, new Symbol(a1), new Member("class"));
        }
        String v = null;
        /*SL:1122*/switch (v-1) {
            case 301: {
                final String a2 = /*EL:1124*/"java.lang.Boolean";
                /*SL:1125*/break;
            }
            case 303: {
                /*SL:1127*/v = "java.lang.Byte";
                /*SL:1128*/break;
            }
            case 306: {
                /*SL:1130*/v = "java.lang.Character";
                /*SL:1131*/break;
            }
            case 334: {
                /*SL:1133*/v = "java.lang.Short";
                /*SL:1134*/break;
            }
            case 324: {
                /*SL:1136*/v = "java.lang.Integer";
                /*SL:1137*/break;
            }
            case 326: {
                /*SL:1139*/v = "java.lang.Long";
                /*SL:1140*/break;
            }
            case 317: {
                /*SL:1142*/v = "java.lang.Float";
                /*SL:1143*/break;
            }
            case 312: {
                /*SL:1145*/v = "java.lang.Double";
                /*SL:1146*/break;
            }
            case 344: {
                /*SL:1148*/v = "java.lang.Void";
                /*SL:1149*/break;
            }
            default: {
                /*SL:1151*/throw new CompileError("invalid builtin type: " + v-1);
            }
        }
        /*SL:1155*/return Expr.make(35, new Symbol(v), new Member("TYPE"));
    }
    
    private ASTree parseMethodCall(final SymbolTable v2, final ASTree v3) throws CompileError {
        /*SL:1167*/if (v3 instanceof Keyword) {
            final int a1 = /*EL:1168*/((Keyword)v3).get();
            /*SL:1169*/if (a1 != 339 && a1 != 336) {
                /*SL:1170*/throw new SyntaxError(this.lex);
            }
        }
        else/*SL:1172*/ if (!(v3 instanceof Symbol)) {
            /*SL:1174*/if (v3 instanceof Expr) {
                final int a2 = /*EL:1175*/((Expr)v3).getOperator();
                /*SL:1176*/if (a2 != 46 && a2 != 35) {
                    /*SL:1177*/throw new SyntaxError(this.lex);
                }
            }
        }
        /*SL:1180*/return CallExpr.makeCall(v3, this.parseArgumentList(v2));
    }
    
    private String toClassName(final ASTree a1) throws CompileError {
        final StringBuffer v1 = /*EL:1186*/new StringBuffer();
        /*SL:1187*/this.toClassName(a1, v1);
        /*SL:1188*/return v1.toString();
    }
    
    private void toClassName(final ASTree v1, final StringBuffer v2) throws CompileError {
        /*SL:1194*/if (v1 instanceof Symbol) {
            /*SL:1195*/v2.append(((Symbol)v1).get());
            /*SL:1196*/return;
        }
        /*SL:1198*/if (v1 instanceof Expr) {
            final Expr a1 = /*EL:1199*/(Expr)v1;
            /*SL:1200*/if (a1.getOperator() == 46) {
                /*SL:1201*/this.toClassName(a1.oprand1(), v2);
                /*SL:1202*/v2.append('.');
                /*SL:1203*/this.toClassName(a1.oprand2(), v2);
                /*SL:1204*/return;
            }
        }
        /*SL:1208*/throw new CompileError("bad static member access", this.lex);
    }
    
    private ASTree parsePrimaryExpr(final SymbolTable v-2) throws CompileError {
        final int value;
        /*SL:1227*/switch (value = this.lex.get()) {
            case 336:
            case 339:
            case 410:
            case 411:
            case 412: {
                /*SL:1233*/return new Keyword(value);
            }
            case 400: {
                final String a1 = /*EL:1235*/this.lex.getString();
                final Declarator v1 = /*EL:1236*/v-2.lookup(a1);
                /*SL:1237*/if (v1 == null) {
                    /*SL:1238*/return new Member(a1);
                }
                /*SL:1240*/return new Variable(a1, v1);
            }
            case 406: {
                /*SL:1242*/return new StringL(this.lex.getString());
            }
            case 328: {
                /*SL:1244*/return this.parseNew(v-2);
            }
            case 40: {
                final ASTree v2 = /*EL:1246*/this.parseExpression(v-2);
                /*SL:1247*/if (this.lex.get() == 41) {
                    /*SL:1248*/return v2;
                }
                /*SL:1250*/throw new CompileError(") is missing", this.lex);
            }
            default: {
                /*SL:1252*/if (isBuiltinType(value) || value == 344) {
                    final int v3 = /*EL:1253*/this.parseArrayDimension();
                    /*SL:1254*/if (this.lex.get() == 46 && this.lex.get() == 307) {
                        /*SL:1255*/return this.parseDotClass(value, v3);
                    }
                }
                /*SL:1258*/throw new SyntaxError(this.lex);
            }
        }
    }
    
    private NewExpr parseNew(final SymbolTable v-3) throws CompileError {
        ArrayInit arrayInit = /*EL:1267*/null;
        int n = /*EL:1268*/this.lex.lookAhead();
        /*SL:1269*/if (isBuiltinType(n)) {
            /*SL:1270*/this.lex.get();
            final ASTList a1 = /*EL:1271*/this.parseArraySize(v-3);
            /*SL:1272*/if (this.lex.lookAhead() == 123) {
                /*SL:1273*/arrayInit = this.parseArrayInitializer(v-3);
            }
            /*SL:1275*/return new NewExpr(n, a1, arrayInit);
        }
        /*SL:1277*/if (n == 400) {
            final ASTList v0 = /*EL:1278*/this.parseClassType(v-3);
            /*SL:1279*/n = this.lex.lookAhead();
            /*SL:1280*/if (n == 40) {
                final ASTList v = /*EL:1281*/this.parseArgumentList(v-3);
                /*SL:1282*/return new NewExpr(v0, v);
            }
            /*SL:1284*/if (n == 91) {
                final ASTList v = /*EL:1285*/this.parseArraySize(v-3);
                /*SL:1286*/if (this.lex.lookAhead() == 123) {
                    /*SL:1287*/arrayInit = this.parseArrayInitializer(v-3);
                }
                /*SL:1289*/return NewExpr.makeObjectArray(v0, v, arrayInit);
            }
        }
        /*SL:1293*/throw new SyntaxError(this.lex);
    }
    
    private ASTList parseArraySize(final SymbolTable a1) throws CompileError {
        ASTList v1 = /*EL:1299*/null;
        /*SL:1300*/while (this.lex.lookAhead() == 91) {
            /*SL:1301*/v1 = ASTList.append(v1, this.parseArrayIndex(a1));
        }
        /*SL:1303*/return v1;
    }
    
    private ASTree parseArrayIndex(final SymbolTable v2) throws CompileError {
        /*SL:1309*/this.lex.get();
        /*SL:1310*/if (this.lex.lookAhead() == 93) {
            /*SL:1311*/this.lex.get();
            /*SL:1312*/return null;
        }
        final ASTree a1 = /*EL:1315*/this.parseExpression(v2);
        /*SL:1316*/if (this.lex.get() != 93) {
            /*SL:1317*/throw new CompileError("] is missing", this.lex);
        }
        /*SL:1319*/return a1;
    }
    
    private ASTList parseArgumentList(final SymbolTable a1) throws CompileError {
        /*SL:1326*/if (this.lex.get() != 40) {
            /*SL:1327*/throw new CompileError("( is missing", this.lex);
        }
        ASTList v1 = /*EL:1329*/null;
        /*SL:1330*/if (this.lex.lookAhead() != 41) {
            while (true) {
                /*SL:1332*/v1 = ASTList.append(v1, this.parseExpression(a1));
                /*SL:1333*/if (this.lex.lookAhead() != 44) {
                    break;
                }
                /*SL:1334*/this.lex.get();
            }
        }
        /*SL:1339*/if (this.lex.get() != 41) {
            /*SL:1340*/throw new CompileError(") is missing", this.lex);
        }
        /*SL:1342*/return v1;
    }
    
    static {
        binaryOpPrecedence = new int[] { 0, 0, 0, 0, 1, 6, 0, 0, 0, 1, 2, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 4, 0 };
    }
}
