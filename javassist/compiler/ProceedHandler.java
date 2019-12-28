package javassist.compiler;

import javassist.compiler.ast.ASTList;
import javassist.bytecode.Bytecode;

public interface ProceedHandler
{
    void doit(JvstCodeGen p0, Bytecode p1, ASTList p2) throws CompileError;
    
    void setReturnType(JvstTypeChecker p0, ASTList p1) throws CompileError;
}
