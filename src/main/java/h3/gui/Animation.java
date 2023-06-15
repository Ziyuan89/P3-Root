package h3.gui;

public interface Animation<N> {

    void start(N start, N end);

    Object getSyncObject();
}
