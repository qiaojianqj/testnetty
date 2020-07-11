package message;

/**
 *
 */
public class Message<T> {
    private int type;
    private int length;
    private T value;
    public Message(int type, int length, T value) {
        this.type = type;
        this.length = length;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "message: type: " + type + ";" + "length: " + length + ";" + "value: " + value + ";";
    }
}
