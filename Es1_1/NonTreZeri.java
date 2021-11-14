public class NonTreZeri{
    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		if (ch == '0')
		    state = 1;
		else if (ch == '1')
		    state = 0;
		else
		    state = -1;
		break;

	    case 1:
		if (ch == '0')
		    state = 2;
		else if (ch == '1')
		    state = 0;
		else
		    state = -1;
		break;

	    case 2:
		if (ch == '0')
		    state = 3;
		else if (ch == '1')
		    state = 0;
		else
		    state = -1;
		break;

	    case 3:
		if (ch == '0' || ch == '1')
		    state = 3;
		else
		    state = -1;
		break;
	    }
	}
	return (state != 3 && state >= 0);  //per capire se non ci sono zeri cambio solamente lo stato finale trasformando i 3 precedenti
    }									//nei casi finali e l'ultimo nello stato pozzo

    public static void main(String[] args){
		
		System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
