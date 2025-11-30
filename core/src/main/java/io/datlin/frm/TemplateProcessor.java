package io.datlin.frm;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.TimeZone;

public class TemplateProcessor {
    private final @Nonnull Configuration configuration;

    public TemplateProcessor() {
        configuration = new Configuration(Configuration.VERSION_2_3_34);

        configuration.setClassForTemplateLoading(this.getClass(), "/templates-v1/");
        configuration.setDefaultEncoding("UTF-8");
        // configuration.setOutputFormat(PlainTextOutputFormat.INSTANCE);
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
        configuration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
    }

    public void process(
        final @Nonnull Map<String, Object> model,
        final @Nonnull String template,
        final @Nonnull String target
    ) {
        // try {
        //     final Path targetPath = Path.of(target);
        //     final Path parent = targetPath.getParent();

        //     if (parent == null) {
        //         throw new IllegalArgumentException("target path %s needs to have parent directory.".formatted(target));
        //     }

        //     // FsUtil.createDirectory(parent);

        //     // Template template1 = configuration.getTemplate(template);

        //     // try (FileWriter fileWriter = new FileWriter(target)) {
        //     //     template1.process(model, fileWriter);
        //     // } catch (IOException | TemplateException e) {
        //     //     throw new RuntimeException(e);
        //     // }
        // } catch (IOException e) {
        //     throw new RuntimeException(e);
        // }
    }
}
