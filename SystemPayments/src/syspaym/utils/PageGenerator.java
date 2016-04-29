package syspaym.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class PageGenerator {
    private static final String HTML_DIR = "public_html/templates";
    private static final Configuration CFG = new Configuration();

    public static String getPage(String filename) {
        Writer stream = new StringWriter();
        try {
            Reader r = new FileReader(HTML_DIR + File.separator + filename);
            char[] buf = new char[1024];
            while(r.read(buf) != -1)
            {
                stream.write(buf);
            }

            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream.toString();
    }

    public static String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = CFG.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        return stream.toString();
    }
}
