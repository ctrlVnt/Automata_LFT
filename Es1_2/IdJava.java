//nota: sono a conoscenza del fatto che molti simboli della tabella aschii e caratteri non inclusi nella
//tastiera italiana sono accettati come nomi di variabile, onde evitare di rischiare di uscire
//fuori traccia e aumentare la difficoltà di lettura del codice mi attenuerò ad un alfabeto
//limitato a quello usato quotidianamente, composto da lettere minuscole, masiuscole e simboli.

public class IdJava{

    public static boolean scan (String s){
        int state = 0;
        int  i= 0;

        while (state >= 0 && i < s.length()){
            final char ch = s.charAt(i++); //scorro la stringa da analizzare

            switch(state){  

                case 0: //PRIMO CARATTERE LETTO
                if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch<= 'z')){ //può accettare tutti i caratteri tranne simboli e numeri
                    state = 2;
                }
                else if (ch == '_' ) //lo tratto a parte perché non posso avere una stringa composta da soli '_'
                    state = 1;
                else
                    state = -1;
                break;

                case 1: //SECONDO CARATTERE LETTO SOLO SE IL PRIMO ERA UN UNDERSCORE
                if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch<= 'z') || (ch >= '0' && ch <= '9')) //può accettare tutti i caratteri tranne simboli
                    state = 2;
                else if (ch == '_') //non posso cambiare stato finché non trovo almeno un carattere diverso da '_'
                    state = 1;
                else 
                    state = -1;
                break;

                case 2: //POSSO ACCETTARE TUTTI I CARATTERI TRANNE I SIMBOLI
                if((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch<= 'z') || (ch >= '0' && ch <= '9') || ch == '_') //accetta tutti i caratteri tranne i simboli
                    state = 2;
                else
                    state = -1; //un simbolo annulla tutta la stringa
                break;
            }
        }
        return state == 2;
    }

    public static void main(String[]args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}