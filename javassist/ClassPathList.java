package javassist;

final class ClassPathList
{
    ClassPathList next;
    ClassPath path;
    
    ClassPathList(final ClassPath a1, final ClassPathList a2) {
        this.next = a2;
        this.path = a1;
    }
}
