package Client;

/**
 * Created by jebush2 on 11/16/2016.
 */
public final class NameFormatter {

    public static final CharSequence[] censored = {"anal","anus","arse","ass","ballsack","balls","bastard","bitch","biatch","bloody","blowjob","blow job","bollock","bollok","boner","boob","bugger","bum","butt","buttplug","clitoris","cock","coon","crap","cunt","damn","dick","dildo","dyke","fag","feck","fellate","fellatio","felching","fuck","f u c k","fudgepacker","fudge packer","flange","goddamn","god damn","hell","homo","jerk","jizz","knobend","knob end","labia","lmao","lmfao","muff","nigger","nigga","omg","penis","piss","poop","prick","pube","pussy","queer","scrotum","sex","shit","s hit","sh1t","slut","smegma","spunk","tit","tosser","turd","twat","vagina","wank","whore","wtf"};
    public static final int MAX_NAME_LENGTH = 11;

    private NameFormatter() {}

    public static String format(String name) {
        for (CharSequence c : censored) {
            if(name.toLowerCase().contains(c)) {
                name = "sunshine";
            }
        }
        name = name.substring(0, Math.min(name.length(), MAX_NAME_LENGTH));
        name = name.trim();
        name = name.replaceAll("\\s+","_");
        return name;
    }

}
