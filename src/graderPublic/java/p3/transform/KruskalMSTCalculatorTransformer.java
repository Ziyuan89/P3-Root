package p3.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
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
            if ("acceptEdge".equals(name) && "(Lp3/graph/Edge;)Z".equals(descriptor) && (access & Opcodes.ACC_STATIC) == 0) {
                return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitCode() {
                        ParameterInterceptor interceptor = new ParameterInterceptor(this);
                        interceptor.interceptParameters(descriptor, access);
                        interceptor.storeArrayRefInList("p3/solver/KruskalMSTCalculatorTests", "acceptEdgeParameters");
                        super.visitCode();
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (opcode == Opcodes.INVOKEVIRTUAL &&
                            "p3/solver/KruskalMSTCalculator".equals(owner) &&
                            "joinGroups".equals(name) &&
                            "(II)V".equals(descriptor)) {
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
