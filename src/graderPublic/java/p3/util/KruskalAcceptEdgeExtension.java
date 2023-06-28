package p3.util;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class KruskalAcceptEdgeExtension implements TestExecutionExceptionHandler {

    private static boolean acceptEdgeCorrect = true;

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        acceptEdgeCorrect = false;
        throw throwable;
    }

    public static boolean isAcceptEdgeCorrect() {
        return acceptEdgeCorrect;
    }
}
