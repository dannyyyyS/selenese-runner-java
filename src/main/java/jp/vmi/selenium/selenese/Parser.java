package jp.vmi.selenium.selenese;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.vmi.selenium.selenese.command.ICommandFactory;
import jp.vmi.selenium.selenese.inject.Binder;
import jp.vmi.selenium.selenese.parser.ParserUtils;
import jp.vmi.selenium.selenese.parser.SeleneseTestProjectReader;
import jp.vmi.selenium.selenese.parser.SideTestProjectReader;
import jp.vmi.selenium.selenese.parser.TestProjectReader;

/**
 * Abstract class of selenese parser.
 */
public abstract class Parser {

    /**
     * Parse input stream.
     *
     * @param filename selenese script file. (Don't use to open a file. It is used as a label and is used to generate filenames based on it)
     * @param is input stream of script file. (test-case or test-suite)
     * @param commandFactory command factory.
     * @param suiteFilter suites to be executed
     * @param caseFilter cases to be executed
     * @return TestCase or TestSuite.
     */
    public static Selenese parse(String filename, InputStream is, ICommandFactory commandFactory, String suiteFilter, String caseFilter) {
        boolean isSide = filename.toLowerCase().endsWith(".side");
        try {
            TestProjectReader projectReader;
            SourceType sourceType;
            if (isSide) {
                sourceType = SourceType.SIDE;
                projectReader = SideTestProjectReader.newInstance(filename, is, suiteFilter, caseFilter);
            } else {
                sourceType = SourceType.SELENESE;
                projectReader = SeleneseTestProjectReader.newInstance(filename, is);
            }
            projectReader.setupTestCaseMap(sourceType, commandFactory);
            return TestProjectParser.parse(sourceType, projectReader, commandFactory);
        } catch (InvalidSeleneseException e) {
            return isSide ? Binder.newErrorTestProject(filename, e) : Binder.newErrorTestSuite(filename, e);
        }
    }

    /**
     * Parse input stream.
     *
     * @param filename selenese script file. (Don't use to open a file. It is used as a label and is used to generate filenames based on it)
     * @param is input stream of script file. (test-case or test-suite)
     * @param commandFactory command factory.
     * @return TestCase or TestSuite.
     */
    public static Selenese parse(String filename, InputStream is, ICommandFactory commandFactory) {
        return parse(filename, is, commandFactory, null, null);
    }

    /**
     * Parse file.
     *
     * @param filename selenese script file. (test-case or test-suite)
     * @param commandFactory command factory.
     * @param suiteFilter suites to be executed
     * @param caseFilter cases to be executed
     * @return TestCase or TestSuite.
     */
    public static Selenese parse(String filename, ICommandFactory commandFactory, String suiteFilter, String caseFilter) {
        try (InputStream is = new FileInputStream(filename)) {
            return parse(filename, is, commandFactory, suiteFilter, caseFilter);
        } catch (IOException e) {
            String name = ParserUtils.getNameFromFilename(filename);
            return Binder.newErrorTestSuite(filename, new InvalidSeleneseException(e.getMessage(), filename, name));
        }
    }

    /**
     * Parse file.
     *
     * @param filename selenese script file. (test-case or test-suite)
     * @param commandFactory command factory.
     * @return TestCase or TestSuite.
     */
    public static Selenese parse(String filename, ICommandFactory commandFactory) {
        return parse(filename, commandFactory, null, null);
    }

    protected abstract Selenese parse(ICommandFactory commandFactory);
}
