package com.momo.pink.test.etcd;

import com.opentable.etcd.EtcdConfiguration;
import com.opentable.etcd.EtcdInstance;
import org.apache.commons.io.IOUtils;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class EtcdFactoryBean implements SmartLifecycle {
    private EtcdInstance instance;

    @Override
    public void start() {
        EtcdConfiguration configuration = new EtcdConfiguration();
        //configuration.setDiscoveryUri("");
        Path dir;
        try {
            dir = Files.createTempDirectory("etcd");
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        dir.toFile().deleteOnExit();
        configuration.setDataDirectory(dir);
        configuration.setDestroyNodeOnExit(true);
        configuration.setClientPort(4001);
        configuration.setHostname("127.0.0.1");
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            System.setProperty("os.name", "Windows");
        }
        instance = new EtcdInstance(configuration);
        System.setProperty("os.name", osName);
        try {
            instance.start();
            waitForServerInit();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public synchronized String getConnectString() {
        if (!instance.isRunning()) {
            throw new IllegalStateException("etcd server was not started");
        }
        return "http://127.0.0.1:" + instance.getClientPort();
    }

    private void waitForServerInit() throws IOException {
        final URL versionUrl = new URL(getConnectString() + "/version");
        IOException exc = null;
        for (int i = 0; i < 100; i++) {
            try {
                IOUtils.toString(versionUrl.openStream(), StandardCharsets.UTF_8);
                exc = null;
                break;
            } catch (IOException e) {
                exc = e;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        if (exc != null) {
            throw exc;
        }
    }

    @Override
    public void stop() {
        if (this.instance != null) {
            this.instance.stop();
        }
    }

    @Override
    public boolean isRunning() {
        return this.instance != null;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {

    }

    @Override
    public int getPhase() {
        return 7;
    }
}
