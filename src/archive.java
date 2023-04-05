import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class archive {
    static long numeroN = 0;
    static long fiN = 1;
    static long numeroE = 2;

    public static void main(String[] args) throws Exception {

        Scanner leer = new Scanner(System.in);
        boolean banderita = false;
        int mensaje;
        boolean bandera = false;
        int numerop = 0, numeroq = 0;

        System.out.println("------CIFRADO RSA------");    
        System.out.println("Ingrese dos números primos: ");

        //Determinar si el primer número es primo
        while(bandera == false){
            System.out.println("Numero 1: ");   
            numerop = leer.nextInt();

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
                numeroq = leer.nextInt();

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
        mensaje = leer.nextInt();

        System.out.println("Mensaje cifrado: " + cifrar(mensaje, publica));
        
       }else if(op == 2){  
        System.out.println("Ingrese el mensaje a descifrar (solo numeros): ");
        mensaje = leer.nextInt();

        System.out.println("Mensaje descifrado: " + descifrar(mensaje, privada));
       }
    }

    public static clave clavePublica(int numeroP, int numeroQ){
        
        boolean banderita = false;
        boolean bien = false;
        Scanner leer = new Scanner(System.in);

        //Paso 2: multiplicar n * q
        numeroN = numeroP*numeroQ;

        //Paso 3: calcular fi(n) = (p-1)(q-1)
        fiN = (numeroP - 1)*(numeroQ - 1);
        
        //Lista de posibles números para e
        ArrayList<Long> listaE = new ArrayList<>();

        while(numeroE < fiN){
            if(MCDeuclides(numeroE, fiN) == 1){
                listaE.add(numeroE);
                numeroE++;
            }else{
                numeroE++;
            }
        }

        System.out.println("\nPosibles valores para e:\n" + listaE.toString());
        
        //Escoger un número de la lista
        while(banderita == false){
            System.out.println("\nEscoja un valor e. Asegúrate de que se encuentre en la lista: ");
            numeroE = leer.nextLong();
            
            if(listaE.contains(numeroE)){
                banderita = true;
            }else{
                System.out.println("Valor incorrecto.");
                
            }
        }

        clave cPublica = new clave(numeroN, numeroE);

        return cPublica;
    }

    /*public static long cifrar(long mensaje, clave clavePublica){
        long cifrado = 0; 
        return cifrado = ((long) Math.pow(mensaje, clavePublica.k)) % clavePublica.n;
    }

    public static clave clavePrivada(){

        long numeroD = (long) inversoModular((int) numeroE,(int) fiN);
        clave cPrivada = new clave(numeroN, numeroD);

        return cPrivada;
    }
*/
    public static long descifrar(long mensaje, clave clavePrivada){
        long descifrado;
        return descifrado = (long) ((Math.pow(mensaje, clavePrivada.k)) % clavePrivada.n);
    }
¨
public static BigInteger cifrar(String mensaje, clave clavePublica){

    BigInteger msj = new BigInteger(mensaje);
    BigInteger cifrado = msj.modPow(clavePublica.k, clavePublica.n);

    return cifrado;
}

//Recursos
    public static boolean esPrimo(int numero) {
            
        if(numero == 1){
            return true;
        }
        for (int i = 2; i < numero/2; i++) {
            if(numero % i == 0){
                return false;
            }
        }

    return true;
    }

    public static long MCDeuclides(long numero1, long numero2){

        long residuo = 1;
        
        while(residuo > 0){
  
              residuo = numero1 % numero2;
              numero1 = numero2;

              if(residuo != 0){
                numero2 = residuo;
              }else{
                break;
              }     
            }          
    
        return numero2;
    }

    public static int inversoModular(int a, int m) {
        a = a % m;
        for(int x = 1; x < m; x++) {
            if((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }
}
