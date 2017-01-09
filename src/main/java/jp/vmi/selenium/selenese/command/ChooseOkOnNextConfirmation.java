package jp.vmi.selenium.selenese.command;

import jp.vmi.selenium.selenese.Context;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.selenese.result.Success;

/**
 * Command "echo".
 */
public class ChooseOkOnNextConfirmation extends AbstractCommand {

    ChooseOkOnNextConfirmation(int index, String name, String... args) {
        super(index, name, args);
    }

    @Override
    protected Result executeImpl(Context context, String... curArgs) {
        context.getJSLibrary().setNextConfirmationState(context.getWrappedDriver(), true);
        return new Success();
    }
}
