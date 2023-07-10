package p3.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class AdjacencyMatrixTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "AdjacencyMatrixTransformer";
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        if (reader.getClassName().equals("p3/graph/AdjacencyMatrix")) {
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
            if ("addEdge".equals(name) && "(III)V".equals(descriptor) && (access & Opcodes.ACC_STATIC) == 0) {
                return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitCode() {
                        ParameterInterceptor interceptor = new ParameterInterceptor(this);
                        interceptor.interceptParameters(descriptor, access);
                        interceptor.storeArrayRefInList("p3/graph/AdjacencyGraphTests", "addEdgeParameters");
                        super.visitCode();
                    }
                };
            } else {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }
    }
}
