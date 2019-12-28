package javassist.bytecode.annotation;

public interface MemberValueVisitor
{
    void visitAnnotationMemberValue(AnnotationMemberValue p0);
    
    void visitArrayMemberValue(ArrayMemberValue p0);
    
    void visitBooleanMemberValue(BooleanMemberValue p0);
    
    void visitByteMemberValue(ByteMemberValue p0);
    
    void visitCharMemberValue(CharMemberValue p0);
    
    void visitDoubleMemberValue(DoubleMemberValue p0);
    
    void visitEnumMemberValue(EnumMemberValue p0);
    
    void visitFloatMemberValue(FloatMemberValue p0);
    
    void visitIntegerMemberValue(IntegerMemberValue p0);
    
    void visitLongMemberValue(LongMemberValue p0);
    
    void visitShortMemberValue(ShortMemberValue p0);
    
    void visitStringMemberValue(StringMemberValue p0);
    
    void visitClassMemberValue(ClassMemberValue p0);
}
