package top.rrricardo.postletterserver.exceptions;

public class DeviceException extends Exception {
    private final String hostname;

    public DeviceException(String hostname) {
        super();
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }
}
