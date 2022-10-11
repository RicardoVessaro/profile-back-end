package estudo.s.ipsumintegrationtest.assertion.pagination;

public class Links {
    
    private Object first;

    private Object prev;

    private Object self;

    private Object next;

    private Object last;

    private Object create;

    public Links() {}

    public Links withFirst(Object first) {
        this.first = first;

        return this;
    }

    public Links withPrev(Object prev) {
        this.prev = prev;

        return this;
    }

    public Links withSelf(Object self) {
        this.self = self;

        return this;
    }

    public Links withNext(Object next) {
        this.next = next;

        return this;
    }

    public Links withLast(Object last) {
        this.last = last;

        return this;
    }

    public Links withCreate(Object create) {
        this.create = create;

        return this;
    }

    public Object getFirst() {
        return first;
    }

    public Object getPrev() {
        return prev;
    }

    public Object getSelf() {
        return self;
    }

    public Object getNext() {
        return next;
    }

    public Object getLast() {
        return last;
    }

    public Object getCreate() {
        return create;
    }

}
