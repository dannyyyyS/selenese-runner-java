package jp.vmi.selenium.selenese;

import jp.vmi.junit.result.ITestCase;
import jp.vmi.selenium.selenese.inject.ExecuteTestCase;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.selenese.utils.LogRecorder;

/**
 *
 */
public class ErrorTestCase extends ErrorSource implements ITestCase {

    @Override
    public LogRecorder getLogRecorder() {
        return null;
    }

    @Override
    public ErrorTestCase initialize(String filename, InvalidSeleneseException e) {
        return (ErrorTestCase) super.initialize(filename, e);
    }

    @ExecuteTestCase
    @Override
    public Result execute(Selenese parent, Runner runner) throws InvalidSeleneseException {
        return super.execute(parent, runner);
    }
}
