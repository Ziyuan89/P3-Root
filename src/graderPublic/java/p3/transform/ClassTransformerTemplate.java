package p3.transform;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

import java.util.function.Function;

public class ClassTransformerTemplate implements ClassTransformer {

    private final String name;
    private final Function<ClassWriter, ClassVisitor> classVisitorSupplier;

    public ClassTransformerTemplate(String name, Function<ClassWriter, ClassVisitor> classVisitorSupplier) {
        this.name = name;
        this.classVisitorSupplier = classVisitorSupplier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        reader.accept(classVisitorSupplier.apply(writer), ClassReader.SKIP_DEBUG);
    }
}
