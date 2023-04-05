
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class App {

    static BigInteger numeroN = BigInteger.ZERO;
    static BigInteger fiN = BigInteger.ONE;
    static BigInteger numeroE = BigInteger.TWO;

    public static void main(String[] args) throws Exception {

        Scanner leer = new Scanner(System.in);
        boolean banderita = false;
        String mensaje = "";
        boolean bandera = false;
        boolean salir = false;
        BigInteger numerop = BigInteger.ZERO, numeroq = BigInteger.ZERO;

        System.out.println("------CIFRADO RSA------");    
        System.out.println("Ingrese dos números primos: ");

        //Determinar si el primer número es primo
        while(bandera == false){
            System.out.println("Numero 1: ");   
            numerop = new BigInteger(leer.nextLine());

            if(esPrimo(numerop)){
                bandera = true;
            }else{
                System.out.println("El numero ingresado no es primo.");  
            }
        }
        
        //Determinar si el segundo numero es primo
        bandera = false;

        while(bandera == false){
            System.out.println("Numero 2: ");   
                numeroq = new BigInteger(leer.nextLine());

                if(esPrimo(numeroq)){
                    bandera = true;
                }else{
                    System.out.println("El numero ingresado no es primo.");  
                }
            }
        
        //Generacion de claves
        clave publica = clavePublica(numerop, numeroq);
        clave privada = clavePrivada();


        System.out.println("Clave pública: (" + publica.n + ", " + publica.k + ")");
        System.out.println("Clave privada: (" + privada.n + ", " + privada.k + ")");
        
        do{
            System.out.println("\n¿Qué desea hacer? ");   
            System.out.println("1.Cifrar\n2.Descifrar\n3.Salir");
            int op = 0; 

            switch(op){
                case 1:
                    System.out.println("Ingrese el mensaje a cifrar: ");
                    mensaje = leer.nextLine();
                    mensaje = leer.nextLine();

                    BigInteger[] textoSalida = cifrar(mensaje, publica);
                
                    System.out.println("\nMensaje cifrado: ");

                    for(int i = 0; i < textoSalida.length; i++){
                        System.out.print(textoSalida[i].toString() + " ");
                    }
                    break;

                case 2:
                    System.out.println("Ingrese el mensaje a descifrar: ");
                    mensaje = leer.nextLine();
                    mensaje = leer.nextLine();
            
                    String letra = "";
                    StringTokenizer string = new StringTokenizer(mensaje);
                    BigInteger[] textoCifrado = new BigInteger[string.countTokens()];
            
                    for (int i = 0; i < textoCifrado.length; i++) {
                        letra = string.nextToken();
                        textoCifrado[i] = new BigInteger(letra);
                    }
            
                    System.out.println("\nMensaje descifrado: " + descifrar(textoCifrado, privada));
                    break;

                case 3:
                    salir = true;
                    break;

                default:
                    System.out.println("Opcion incorrecta.");
                    }

        }while(!salir);
 
    }

    public static clave clavePublica(BigInteger numeroP, BigInteger numeroQ){
        
        boolean banderita = false;
        boolean bien = false;
        Scanner leer = new Scanner(System.in);
        BigInteger uno = BigInteger.ONE;

        //Paso 2: multiplicar n * q
        numeroN = numeroP.multiply(numeroQ);

        //Paso 3: calcular fi(n) = (p-1)(q-1)
        fiN = numeroP.subtract(uno).multiply(numeroQ.subtract(uno));
         //(numeroP - 1)*(numeroQ - 1);
        
        //Lista de posibles números para e
        ArrayList<BigInteger> listaE = new ArrayList<>();
    
        while(numeroE.compareTo(fiN) == -1) {
            if(MCDeuclides(numeroE, fiN).equals(BigInteger.ONE)) {

                listaE.add(numeroE);
                numeroE = numeroE.add(BigInteger.ONE);
            }
            numeroE = numeroE.add(BigInteger.ONE);
        }

        System.out.println("\nPosibles valores para e:\n" + listaE.toString());
        
        //Escoger un número de la lista
        while(banderita == false){
            System.out.println("\nEscoja un valor e. Asegúrate de que se encuentre en la lista: ");
            numeroE = new BigInteger(leer.nextLine());
            
            if(listaE.contains(numeroE)){
                banderita = true;
            }else{
                System.out.println("Valor incorrecto.");
                
            }
        }

        clave cPublica = new clave(numeroN, numeroE);

        return cPublica;
    }

    public static BigInteger[] cifrar(String mensaje, clave clavePublica){

        int i;
        byte[] temp = new byte[1];
        byte[] elementos = mensaje.getBytes();
        BigInteger[] msj = new BigInteger[elementos.length];
        
        //recorrer el tamaño de los bigdigitos
        for(i = 0; i < msj.length; i++){
            temp[0] = elementos[i];
            msj[i] = new BigInteger(temp);
        }

        BigInteger[] cifrado = new BigInteger[msj.length];

        for(i = 0; i < msj.length; i++){
            cifrado[i] = msj[i].modPow(clavePublica.k, clavePublica.n);
        }

        return cifrado;
    }

    public static clave clavePrivada(){

        BigInteger numeroD = numeroE.modInverse(fiN);
        clave cPrivada = new clave(numeroN, numeroD);

        return cPrivada;
    }

    public static String descifrar(BigInteger[] cifrado, clave clavePrivada){

        BigInteger[] descifrado = new BigInteger[cifrado.length];

        for(int i = 0; i < cifrado.length; i++){
            descifrado[i] = cifrado[i].modPow(clavePrivada.k, clavePrivada.n);
        }

        char[] charArray = new char[descifrado.length];

        for(int i = 0; i < charArray.length; i++){
            charArray[i] = (char) descifrado[i].intValue();
        }

        return (new String(charArray));
    }

//Recursos
    public static boolean esPrimo(BigInteger numero) {
            
        if(numero.equals(1)){
            return true;
        }

        BigInteger dos = BigInteger.valueOf(2);
        BigInteger limite = numero.divide(dos);

        for (BigInteger i = dos; i.compareTo(limite) < 0; i = i.add(BigInteger.ONE)) {
            if(numero.mod(i).equals(BigInteger.ZERO)) {
                return false;
            }
}
    return true;
    }

    public static BigInteger MCDeuclides(BigInteger numero1, BigInteger numero2){

        BigInteger residuo = BigInteger.ONE;
        
        while(residuo.compareTo(BigInteger.ZERO) > 0){
      
              residuo = numero1.mod(numero2);
              numero1 = numero2;
    
              if(residuo.compareTo(BigInteger.ZERO) != 0){
                numero2 = residuo;
              }else{
                break;
              }     
            }          
    
        return numero2;
    } 

    public static BigInteger inversoModular(BigInteger a, BigInteger m) {
        a = a.mod(m);
        for (BigInteger x = BigInteger.ONE; x.compareTo(m) < 0; x = x.add(BigInteger.ONE)) {
            if (a.multiply(x).mod(m).equals(BigInteger.ONE)) {
                return x;
            }
        }
        return BigInteger.valueOf(-1);
    }
}
