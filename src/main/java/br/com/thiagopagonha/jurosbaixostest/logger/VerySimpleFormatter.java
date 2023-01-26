package br.com.thiagopagonha.jurosbaixostest.logger;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class VerySimpleFormatter extends Formatter {
    private static final String PATTERN = "HH:mm:ss.SSS";

    @Override
    public String format(final LogRecord record) {
        return String.format(
                "%1$s %2$-7s %3$s\n",
                new java.text.SimpleDateFormat(PATTERN).format(
                        new Date(record.getMillis())),
                record.getLevel().getName(), formatMessage(record));
    }
}
