import java.util.ArrayList;

public class Main {

    public static void main(String[] args){

        String received1 = "0xC6 0x57 0x54 0x95 0x5E 0x9E 0x6B 0xC6 0x55 0x17 0x55 0x52 0x9E 0x21";
        String expected1 = "0xC6 0x55 0x17 0x55 0x52 0x9E 0x6B 0xC6 0x55 0xD4 0x95 0x76 0x9E 0x21";

        String received2 = "0xC6 0xA7 0x15 0xC5 0x2D 0x6A 0x21";
        String expected2 = "0xC6 0x5A 0x94 0xB5 0x72 0x9C 0x21";

        String received3 = "0xC6 0x56 0xD7 0x55 0x29 0x5C 0x6B 0xC6 0xA7 0x95 0x5A 0x56 0x9E 0x21";
        String expected3 = "0xC6 0xA5 0x55 0x5A 0x79 0x5C 0x6B 0xC6 0x52 0x97 0x55 0x6E 0x9E 0x21";

        Encoder encoder = new Encoder();

        //Decode message

        ArrayList<String> decode_step1 = encoder.divideBytesIntoHexPackets(received1);
        ArrayList<String> decode_step2a = encoder.hexListToBinList(decode_step1);
        ArrayList<String> decode_step2b = encoder.binListDecoderTable1(decode_step2a);
        ArrayList<String> decode_step2c = encoder.binListToHexList(decode_step2b);
        StringBuilder decode_step3 = new StringBuilder();
        for(String s : decode_step2c){
            decode_step3.append(s);
            decode_step3.append(" ");
        }

        String decode_step4 = encoder.hexToAscii(decode_step3.toString());

        //Invert message
        String decode_step5 = encoder.invertAscii(decode_step4);


        //Encode inverted message
        StringBuilder encode_step1 = new StringBuilder(decode_step5);
        encode_step1.append(" ");
        String encode_step2 = encoder.asciiToHex(encode_step1.toString());
        ArrayList<String> encode_step3a = encoder.invertHexList(decode_step2c);
        ArrayList<String> encode_step3b = encoder.hexListToBinList(encode_step3a);
        ArrayList<String> encode_step3c = encoder.binListEncodeTable1(encode_step3b);
        ArrayList<String> encode_step3d = encoder.binListToHexList(encode_step3c);
        ArrayList<String> encode_step4 = encoder.listAddCode(encode_step3d);
        String encode_step5 = encoder.listToString(encode_step4);
        String encode_step6 = encoder.hexToAscii(encode_step5);




        System.out.println("\n===Decode===\n");
        System.out.println("Step 1:\t\t" + decode_step1);
        System.out.println("Step 2.1:\t" + decode_step2a);
        System.out.println("Step 2.2:\t" + decode_step2b);
        System.out.println("Step 2.3:\t" + decode_step2c);
        System.out.println("Step 3:\t\t" + decode_step3);
        System.out.println("Step 4:\t\t" + decode_step4);
        System.out.println("Step 5:\t\t" + decode_step5);
        System.out.println("\n===Encode===\n");
        System.out.println("Step 1:\t\t" + encode_step1);
        System.out.println("Step 2:\t\t" + encode_step2);
        System.out.println("Step 3.1:\t" + encode_step3a);
        System.out.println("Step 3.2:\t" + encode_step3b);
        System.out.println("Step 3.3:\t" + encode_step3c);
        System.out.println("Step 3.4:\t" + encode_step3d);
        System.out.println("Step 4:\t\t" + encode_step4);
        System.out.println("Step 5:\t\t" + encode_step5);
        System.out.println("Step 6:\t\t" + encode_step6);

        System.out.println("\n\n\nEsperado:\t" + received1);
        System.out.println("Resultado:\t" + encode_step5);
        System.out.println("Entrada:\t" + received3);

    }
}
