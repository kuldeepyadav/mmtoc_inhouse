package mmtoc.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.URL;

public class Utils{



public static void downloadUsingProxy(final Proxy proxy, final URL url, final File file) throws IOException {
        try (
            final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            final InputStream inputStream = url.openConnection(proxy).getInputStream()) {
            final byte[] buffer = new byte[65536];
            while (true) {
                final int len = inputStream.read(buffer);
                if (len < 0) {
                    break;
                }
                outputStream.write(buffer, 0, len);
            }
        } catch (final IOException ex) {
            file.delete();
            throw ex;
        }
    }
}