package p3.transform;

import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class KruskalMSTCalculatorTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "KruskalMSTCalculatorTransformer";
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        if (reader.getClassName().equals("p3/solver/KruskalMSTCalculator")) {
            reader.accept(new Transformer(writer), ClassReader.SKIP_DEBUG);
        } else {
            reader.accept(writer, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }
    }

    private static class Transformer extends ClassVisitor {

        protected Transformer(ClassVisitor classVisitor) {
            super(Opcodes.ASM9, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (name.equals("calculateMST") && descriptor.equals("()Lp3/graph/Graph;") || name.startsWith("lambda$calculateMST$")) {
                return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (opcode == Opcodes.INVOKEVIRTUAL &&
                            owner.equals("p3/solver/KruskalMSTCalculator") &&
                            name.equals("acceptEdge") &&
                            descriptor.equals("(Lp3/graph/Edge;)Z")) {
                            ParameterInterceptor interceptor = new ParameterInterceptor(this);
                            interceptor.interceptParameters(new Type[] {Type.getObjectType("p3/graph/Edge")});
                            interceptor.storeArrayRefInList("p3/solver/KruskalMSTCalculatorTests", "acceptEdgeParameters");
                        }
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                };
            } else if (name.equals("acceptEdge") && descriptor.equals("(Lp3/graph/Edge;)Z")) {
                return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (opcode == Opcodes.INVOKEVIRTUAL &&
                            owner.equals("p3/solver/KruskalMSTCalculator") &&
                            name.equals("joinGroups") &&
                            descriptor.equals("(II)V")) {
                            super.visitInsn(Opcodes.ICONST_1);
                            super.visitFieldInsn(Opcodes.PUTSTATIC,
                                "p3/solver/KruskalMSTCalculatorTests",
                                "calledJoinGroups",
                                "Z");
                        }
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                };
            } else {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }
    }
}
