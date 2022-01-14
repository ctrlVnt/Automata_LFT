public class NumberTok extends Token {

    public static int number;

	public NumberTok (int tag, int i){
        super(tag);
        number = i;
    }

    public String toString(){ 
        return "<" + tag + ", " + number + ">";
    }
}
