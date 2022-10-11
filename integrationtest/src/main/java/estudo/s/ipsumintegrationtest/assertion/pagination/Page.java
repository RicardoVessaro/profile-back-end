package estudo.s.ipsumintegrationtest.assertion.pagination;

public class Page {
    
    private int size;
    
    private int totalElements;

    private int totalPages;

    private int number;

    public Page() {
    }

    public Page withSize(int size) {
        this.size = size;

        return this;
    }

    public Page withTotalElements(int totalElements) {
        this.totalElements = totalElements;

        return this;
    }

    public Page withTotalPages(int totalPages) {
        this.totalPages = totalPages;

        return this;
    }

    public Page withNumber(int number) {
        this.number = number;

        return this;
    }

    public int getSize() {
        return size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getNumber() {
        return number;
    }

}
