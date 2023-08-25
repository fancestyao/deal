package study.neo.deal.exception;

public class ConflictDataRequest extends RuntimeException {
    public ConflictDataRequest(String message) {
        super(message);
    }
}