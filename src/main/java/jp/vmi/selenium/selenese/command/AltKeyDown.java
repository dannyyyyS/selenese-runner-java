package jp.vmi.selenium.selenese.command;

import jp.vmi.selenium.selenese.Context;
import jp.vmi.selenium.selenese.result.Result;
import jp.vmi.selenium.selenese.result.Success;

/**
 * Command "altKeyDown".
 */
public class AltKeyDown extends AbstractCommand {

    AltKeyDown(int index, String name, String... args) {
        super(index, name, args);
    }

    @Override
    protected Result executeImpl(Context context, String... curArgs) {
        context.getModifierKeyState().altKeyDown();
        return new Success();
    }
}
