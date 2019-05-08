package xjtucad.model;

public class SSOConf {
    private int port=6030;

    private String remoteHost ="127.0.0.1";

    private int remoteTomcatPort=8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemoteTomcatPort() {
        return remoteTomcatPort;
    }

    public void setRemoteTomcatPort(int remoteTomcatPort) {
        this.remoteTomcatPort = remoteTomcatPort;
    }


}
