package jp.vmi.selenium.selenese.command;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import jp.vmi.selenium.selenese.Context;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.selenese.result.Success;

import static jp.vmi.selenium.selenese.command.ArgumentType.*;

/**
 * Command "check".
 */
public class Check extends AbstractCommand {

    private static final int ARG_LOCATOR = 0;

    Check(int index, String name, String... args) {
        super(index, name, args, LOCATOR);
    }

    @Override
    protected Result executeImpl(Context context, String... curArgs) {
        String locator = curArgs[ARG_LOCATOR];
        WebDriver driver = context.getWrappedDriver();
        WebElement element = context.getElementFinder().findElement(driver, locator);
        context.getJSLibrary().replaceAlertMethod(driver, element);
        if (!element.isSelected())
            element.click();
        return new Success();
    }
}
