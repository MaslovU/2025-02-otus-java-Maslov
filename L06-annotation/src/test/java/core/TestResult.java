package core;

public class TestResult {

    private int total;
    private int unsuccessfulTestCounter;
    private int successfulTestCounter;

    public TestResult(int total, int unsuccessfulTestCounter, int successfulTestCounter) {
        this.total = total;
        this.unsuccessfulTestCounter = unsuccessfulTestCounter;
        this.successfulTestCounter = successfulTestCounter;
    }

    public TestResult() {
    }

    public int getTotal() {
        return total;
    }

    public int getUnsuccessfulTestCounter() {
        return unsuccessfulTestCounter;
    }

    public int getSuccessfulTestCounter() {
        return successfulTestCounter;
    }
}
