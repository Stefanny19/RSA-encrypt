
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    static BigInteger numeroN = BigInteger.ZERO;
    static BigInteger fiN = BigInteger.ONE;
    static BigInteger numeroE = BigInteger.TWO;

    public static void main(String[] args) throws Exception {

        Scanner leer = new Scanner(System.in);
        boolean banderita = false;
        String mensaje = "";
        boolean bandera = false;
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
        
        
        System.out.println("\n¿Qué desea hacer? ");   
        System.out.println("1.Cifrar\n2.Descifrar");
        int op = 0;
        
        while(banderita == false){
            op = leer.nextInt();

            if(op == 1 || op == 2){
                banderita = true;
            }else{
                System.out.println("Opcion incorrecta.");    
            }
        }

       if(op == 1){

        System.out.println("Ingrese el mensaje a cifrar (solo numeros): ");
        mensaje = leer.nextLine();
        mensaje = leer.nextLine();
    
        System.out.println("Mensaje cifrado: " + cifrar(mensaje, publica));
        
       }else if(op == 2){  
        System.out.println("Ingrese el mensaje a descifrar (solo numeros): ");
        mensaje = leer.nextLine();
        mensaje = leer.nextLine();

        System.out.println("Mensaje descifrado: " + descifrar(mensaje, privada));
       }
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

    public static BigInteger cifrar(String mensaje, clave clavePublica){

        BigInteger msj = new BigInteger(mensaje);
        BigInteger cifrado = msj.modPow(clavePublica.k, clavePublica.n);

        return cifrado;
    }

    public static clave clavePrivada(){

        BigInteger numeroD = numeroE.modInverse(fiN);
        clave cPrivada = new clave(numeroN, numeroD);

        return cPrivada;
    }

    public static BigInteger descifrar(String mensaje, clave clavePrivada){

        BigInteger msj = new BigInteger(mensaje);
        BigInteger cifrado = msj.modPow(clavePrivada.k, clavePrivada.n);

        return cifrado;
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
