package javassist.bytecode.analysis;

import java.util.ArrayList;
import javassist.bytecode.stackmap.BasicBlock;
import javassist.bytecode.BadBytecode;
import javassist.CtMethod;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;

public class ControlFlow
{
    private CtClass clazz;
    private MethodInfo methodInfo;
    private Block[] basicBlocks;
    private Frame[] frames;
    
    public ControlFlow(final CtMethod a1) throws BadBytecode {
        this(a1.getDeclaringClass(), a1.getMethodInfo2());
    }
    
    public ControlFlow(final CtClass v-6, final MethodInfo v-5) throws BadBytecode {
        this.clazz = v-6;
        this.methodInfo = v-5;
        this.frames = null;
        this.basicBlocks = (Block[])new BasicBlock.Maker() {
            @Override
            protected BasicBlock makeBlock(final int a1) {
                /*SL:68*/return new Block(a1, ControlFlow.this.methodInfo);
            }
            
            @Override
            protected BasicBlock[] makeArray(final int a1) {
                /*SL:71*/return new Block[a1];
            }
        }.make(v-5);
        if (this.basicBlocks == null) {
            this.basicBlocks = new Block[0];
        }
        final int length = this.basicBlocks.length;
        final int[] array = new int[length];
        for (Block a2 = (Block)0; a2 < length; ++a2) {
            a2 = this.basicBlocks[a2];
            a2.index = a2;
            a2.entrances = new Block[a2.incomings()];
            array[a2] = 0;
        }
        for (int i = 0; i < length; ++i) {
            final Block block = this.basicBlocks[i];
            for (int v0 = 0; v0 < block.exits(); ++v0) {
                final Block v = block.exit(v0);
                v.entrances[array[v.index]++] = block;
            }
            final Catcher[] v2 = block.catchers();
            for (int v3 = 0; v3 < v2.length; ++v3) {
                final Block v4 = v2[v3].node;
                v4.entrances[array[v4.index]++] = block;
            }
        }
    }
    
    public Block[] basicBlocks() {
        /*SL:107*/return this.basicBlocks;
    }
    
    public Frame frameAt(final int a1) throws BadBytecode {
        /*SL:119*/if (this.frames == null) {
            /*SL:120*/this.frames = new Analyzer().analyze(this.clazz, this.methodInfo);
        }
        /*SL:122*/return this.frames[a1];
    }
    
    public Node[] dominatorTree() {
        final int length = /*EL:146*/this.basicBlocks.length;
        /*SL:147*/if (length == 0) {
            /*SL:148*/return null;
        }
        final Node[] array = /*EL:150*/new Node[length];
        final boolean[] array2 = /*EL:151*/new boolean[length];
        final int[] v0 = /*EL:152*/new int[length];
        /*SL:153*/for (int v = 0; v < length; ++v) {
            /*SL:154*/array[v] = new Node(this.basicBlocks[v]);
            /*SL:155*/array2[v] = false;
        }
        final Access v2 = /*EL:158*/new Access(array) {
            @Override
            BasicBlock[] exits(final Node a1) {
                /*SL:159*/return a1.block.getExit();
            }
            
            @Override
            BasicBlock[] entrances(final Node a1) {
                /*SL:160*/return a1.block.entrances;
            }
        };
        /*SL:162*/array[0].makeDepth1stTree(null, array2, 0, v0, v2);
        /*SL:166*/do {
            for (int v3 = 0; v3 < length; ++v3) {
                array2[v3] = false;
            }
        } while (array[0].makeDominatorTree(array2, v0, v2));
        /*SL:167*/setChildren(array);
        /*SL:168*/return array;
    }
    
    public Node[] postDominatorTree() {
        final int length = /*EL:192*/this.basicBlocks.length;
        /*SL:193*/if (length == 0) {
            /*SL:194*/return null;
        }
        final Node[] array = /*EL:196*/new Node[length];
        final boolean[] array2 = /*EL:197*/new boolean[length];
        final int[] v0 = /*EL:198*/new int[length];
        /*SL:199*/for (int v = 0; v < length; ++v) {
            /*SL:200*/array[v] = new Node(this.basicBlocks[v]);
            /*SL:201*/array2[v] = false;
        }
        final Access v2 = /*EL:204*/new Access(array) {
            @Override
            BasicBlock[] exits(final Node a1) {
                /*SL:205*/return a1.block.entrances;
            }
            
            @Override
            BasicBlock[] entrances(final Node a1) {
                /*SL:206*/return a1.block.getExit();
            }
        };
        int v3 = /*EL:209*/0;
        /*SL:210*/for (int v4 = 0; v4 < length; ++v4) {
            /*SL:211*/if (array[v4].block.exits() == 0) {
                /*SL:212*/v3 = array[v4].makeDepth1stTree(null, array2, v3, v0, v2);
            }
        }
        boolean v5;
        /*SL:224*/do {
            for (int v6 = 0; v6 < length; ++v6) {
                array2[v6] = false;
            }
            v5 = false;
            for (int v6 = 0; v6 < length; ++v6) {
                if (array[v6].block.exits() == 0 && array[v6].makeDominatorTree(array2, v0, v2)) {
                    v5 = true;
                }
            }
        } while (v5);
        /*SL:226*/setChildren(array);
        /*SL:227*/return array;
    }
    
    public static class Block extends BasicBlock
    {
        public Object clientData;
        int index;
        MethodInfo method;
        Block[] entrances;
        
        Block(final int a1, final MethodInfo a2) {
            super(a1);
            this.clientData = null;
            this.method = a2;
        }
        
        @Override
        protected void toString2(final StringBuffer v2) {
            /*SL:256*/super.toString2(v2);
            /*SL:257*/v2.append(", incoming{");
            /*SL:258*/for (int a1 = 0; a1 < this.entrances.length; ++a1) {
                /*SL:259*/v2.append(this.entrances[a1].position).append(", ");
            }
            /*SL:261*/v2.append("}");
        }
        
        BasicBlock[] getExit() {
            /*SL:264*/return this.exit;
        }
        
        public int index() {
            /*SL:273*/return this.index;
        }
        
        public int position() {
            /*SL:279*/return this.position;
        }
        
        public int length() {
            /*SL:284*/return this.length;
        }
        
        public int incomings() {
            /*SL:289*/return this.incoming;
        }
        
        public Block incoming(final int a1) {
            /*SL:295*/return this.entrances[a1];
        }
        
        public int exits() {
            /*SL:302*/return (this.exit == null) ? 0 : this.exit.length;
        }
        
        public Block exit(final int a1) {
            /*SL:310*/return (Block)this.exit[a1];
        }
        
        public Catcher[] catchers() {
            final ArrayList v1 = /*EL:317*/new ArrayList();
            /*SL:319*/for (Catch v2 = this.toCatch; v2 != null; /*SL:321*/v2 = v2.next) {
                v1.add(new Catcher(v2));
            }
            /*SL:324*/return v1.<Catcher>toArray(new Catcher[v1.size()]);
        }
    }
    
    abstract static class Access
    {
        Node[] all;
        
        Access(final Node[] a1) {
            this.all = a1;
        }
        
        Node node(final BasicBlock a1) {
            /*SL:331*/return this.all[((Block)a1).index];
        }
        
        abstract BasicBlock[] exits(final Node p0);
        
        abstract BasicBlock[] entrances(final Node p0);
    }
    
    public static class Node
    {
        private Block block;
        private Node parent;
        private Node[] children;
        
        Node(final Block a1) {
            this.block = a1;
            this.parent = null;
        }
        
        @Override
        public String toString() {
            final StringBuffer v0 = /*EL:353*/new StringBuffer();
            /*SL:354*/v0.append("Node[pos=").append(this.block().position());
            /*SL:355*/v0.append(", parent=");
            /*SL:356*/v0.append((this.parent == null) ? "*" : Integer.toString(this.parent.block().position()));
            /*SL:357*/v0.append(", children{");
            /*SL:358*/for (int v = 0; v < this.children.length; ++v) {
                /*SL:359*/v0.append(this.children[v].block().position()).append(", ");
            }
            /*SL:361*/v0.append("}]");
            /*SL:362*/return v0.toString();
        }
        
        public Block block() {
            /*SL:368*/return this.block;
        }
        
        public Node parent() {
            /*SL:373*/return this.parent;
        }
        
        public int children() {
            /*SL:378*/return this.children.length;
        }
        
        public Node child(final int a1) {
            /*SL:385*/return this.children[a1];
        }
        
        int makeDepth1stTree(final Node a4, final boolean[] a5, int v1, final int[] v2, final Access v3) {
            final int v4 = /*EL:393*/this.block.index;
            /*SL:394*/if (a5[v4]) {
                /*SL:395*/return v1;
            }
            /*SL:397*/a5[v4] = true;
            /*SL:398*/this.parent = a4;
            final BasicBlock[] v5 = /*EL:399*/v3.exits(this);
            /*SL:400*/if (v5 != null) {
                /*SL:401*/for (int a6 = 0; a6 < v5.length; ++a6) {
                    final Node a7 = /*EL:402*/v3.node(v5[a6]);
                    /*SL:403*/v1 = a7.makeDepth1stTree(this, a5, v1, v2, v3);
                }
            }
            /*SL:406*/v2[v4] = v1++;
            /*SL:407*/return v1;
        }
        
        boolean makeDominatorTree(final boolean[] v-6, final int[] v-5, final Access v-4) {
            final int index = /*EL:411*/this.block.index;
            /*SL:412*/if (v-6[index]) {
                /*SL:413*/return false;
            }
            /*SL:415*/v-6[index] = true;
            boolean b = /*EL:416*/false;
            final BasicBlock[] exits = /*EL:417*/v-4.exits(this);
            /*SL:418*/if (exits != null) {
                /*SL:419*/for (Node a2 = (Node)0; a2 < exits.length; ++a2) {
                    /*SL:420*/a2 = v-4.node(exits[a2]);
                    /*SL:421*/if (a2.makeDominatorTree(v-6, v-5, v-4)) {
                        /*SL:422*/b = true;
                    }
                }
            }
            final BasicBlock[] v0 = /*EL:425*/v-4.entrances(this);
            /*SL:426*/if (v0 != null) {
                /*SL:427*/for (int v = 0; v < v0.length; ++v) {
                    /*SL:428*/if (this.parent != null) {
                        final Node a3 = getAncestor(/*EL:429*/this.parent, v-4.node(v0[v]), v-5);
                        /*SL:430*/if (a3 != this.parent) {
                            /*SL:431*/this.parent = a3;
                            /*SL:432*/b = true;
                        }
                    }
                }
            }
            /*SL:437*/return b;
        }
        
        private static Node getAncestor(Node a1, Node a2, final int[] a3) {
            /*SL:441*/while (a1 != a2) {
                /*SL:442*/if (a3[a1.block.index] < a3[a2.block.index]) {
                    /*SL:443*/a1 = a1.parent;
                }
                else {
                    /*SL:445*/a2 = a2.parent;
                }
                /*SL:447*/if (a1 == null || a2 == null) {
                    /*SL:448*/return null;
                }
            }
            /*SL:451*/return a1;
        }
        
        private static void setChildren(final Node[] v-3) {
            final int length = /*EL:455*/v-3.length;
            final int[] array = /*EL:456*/new int[length];
            /*SL:457*/for (int a1 = 0; a1 < length; ++a1) {
                /*SL:458*/array[a1] = 0;
            }
            /*SL:460*/for (int v0 = 0; v0 < length; ++v0) {
                final Node v = /*EL:461*/v-3[v0].parent;
                /*SL:462*/if (v != null) {
                    final int[] array2 = /*EL:463*/array;
                    final int index = v.block.index;
                    ++array2[index];
                }
            }
            /*SL:466*/for (int v0 = 0; v0 < length; ++v0) {
                /*SL:467*/v-3[v0].children = new Node[array[v0]];
            }
            /*SL:469*/for (int v0 = 0; v0 < length; ++v0) {
                /*SL:470*/array[v0] = 0;
            }
            /*SL:472*/for (int v0 = 0; v0 < length; ++v0) {
                final Node v = /*EL:473*/v-3[v0];
                final Node v2 = /*EL:474*/v.parent;
                /*SL:475*/if (v2 != null) {
                    /*SL:476*/v2.children[array[v2.block.index]++] = v;
                }
            }
        }
    }
    
    public static class Catcher
    {
        private Block node;
        private int typeIndex;
        
        Catcher(final BasicBlock.Catch a1) {
            this.node = (Block)a1.body;
            this.typeIndex = a1.typeIndex;
        }
        
        public Block block() {
            /*SL:496*/return this.node;
        }
        
        public String type() {
            /*SL:503*/if (this.typeIndex == 0) {
                /*SL:504*/return "java.lang.Throwable";
            }
            /*SL:506*/return this.node.method.getConstPool().getClassInfo(this.typeIndex);
        }
    }
}
