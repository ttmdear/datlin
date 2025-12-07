package io.datlin.frm;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.datlin.util.FilesUtil;
import jakarta.annotation.Nonnull;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.TimeZone;

public class TemplateProcessor {
    private @Nonnull final FilesUtil fileUtils;
    private @Nonnull final Configuration configuration;

    public TemplateProcessor(
        @Nonnull final FilesUtil fileUtils
    ) {
        this.fileUtils = fileUtils;

        configuration = new Configuration(Configuration.VERSION_2_3_34);
        configuration.setClassForTemplateLoading(this.getClass(), "/templates/");
        configuration.setDefaultEncoding("UTF-8");
        // configuration.setOutputFormat(PlainTextOutputFormat.INSTANCE);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
        configuration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
    }

    public void process(
        @Nonnull final Map<String, Object> model,
        @Nonnull final String template,
        @Nonnull final String target
    ) {
        try {
            fileUtils.createDirectories(Path.of(target).getParent());

            final Template template1 = configuration.getTemplate(template);

            try (final FileWriter fileWriter = new FileWriter(target)) {
                template1.process(model, fileWriter);
            } catch (IOException | TemplateException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
