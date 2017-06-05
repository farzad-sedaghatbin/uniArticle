package ir.university.toosi.wtms.web.util;


/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class IranSystem2UTF {

    public IranSystem2UTF() {
    }

    public final static String convert(String in) {
        if (in == null) return null;
        StringBuffer out = new StringBuffer();
        String a = "";
        for (int i = 0; i < in.length(); i++) {
            char newchar = (char) convertIranUni((int) in.charAt(i));
            out.append(newchar);
        }
        a = out.reverse().toString();
        return a;
    }

    public final static int convertIranUni(int iransystem) {

        int iransys = iransystem & 0x00FF;
//	    int iransys = iransystem;

        if (iransys < 0x7F) return iransys;
         /* Arab numbers / Arabaj ciferoj */
        if (iransys >= 0x80 && iransys <= 0x89) return (iransys - 0x80 + 0x6f0);

	     /* arabic comma */
        if (iransys == 0x8a) return 0x060C;

	     /* arabic tatweel */
        if (iransys == 0x8B) return 0x0640;

	     /* arabic question mark / demandosigno */
        if (iransys == 0x8C) return 0x061F;

	     /* alef with madda */
        if (iransys == 0x8D) return 0x0622;

	     /* yeh with hamza */
        if (iransys == 0x8E) return 0x0626;

	     /* hamza */
        if (iransys == 0x8F) return 0x0621;

	     /* alef */
        if (iransys == 0x90 || iransys == 0x91) return 0x0627;

	     /* beh */
        if (iransys == 0x92 || iransys == 0x93) return 0x0628;

	     /* peh */
        if (iransys == 0x94 || iransys == 0x95) return 0x067E;

	     /* teh */
        if (iransys == 0x96 || iransys == 0x97) return 0x062A;

	     /* theh */
        if (iransys == 0x98 || iransys == 0x99) return 0x062B;

	     /* jeem */
        if (iransys == 0x9A || iransys == 0x9B) return 0x062C;

	     /* tcheh */
        if (iransys == 0x9C || iransys == 0x9D) return 0x0686;

	     /* hah */
        if (iransys == 0x9E || iransys == 0x9F) return 0x062D;

	     /* khah */
        if (iransys == 0xA0 || iransys == 0xA1) return 0x062E;

	     /* dal */
        if (iransys == 0xA2) return 0x062F;

	     /* thal */
        if (iransys == 0xA3) return 0x0630;

	     /* re */
        if (iransys == 0xA4) return 0x0631;

	     /* zain */
        if (iransys == 0xA5) return 0x0632;

	     /* je */
        if (iransys == 0xA6) return 0x0698;

	     /* seen */
        if (iransys == 0xA7 || iransys == 0xA8) return 0x0633;

	     /* sheen */
        if (iransys == 0xA9 || iransys == 0xAA) return 0x0634;

	     /* sad */
        if (iransys == 0xAB || iransys == 0xAC) return 0x0635;

	     /* dad */
        if (iransys == 0xAD || iransys == 0xAE) return 0x0636;

	     /* ta */
        if (iransys == 0xAF) return 0x0637;

	     /* za */
        if (iransys == 0xE0) return 0x0638;

	     /* ain */
        if (iransys >= 0xE1 && iransys <= 0xE4) return 0x0639;

	     /* ghain */
        if (iransys >= 0xE5 && iransys <= 0xE8) return 0x063A;

	     /* feh */
        if (iransys == 0xE9 || iransys == 0xEA) return 0x0641;

	     /* qaf */
        if (iransys == 0xEB || iransys == 0xEC) return 0x0642;

	     /* keheh (kaf?) */
        if (iransys == 0xED || iransys == 0xEE) return 0x0643;   /* 6A9 is probably wrong, 6A9 probable malgxustas */

	     /* gaf */
        if (iransys == 0xEF || iransys == 0xF0) return 0x06AF;

	     /* lam */
        if (iransys >= 0xF1 && iransys <= 0xF3) return 0x0644;

	     /* meem */
        if (iransys == 0xF4 || iransys == 0xF5) return 0x0645;

	     /* noon */
        if (iransys == 0xF6 || iransys == 0xF7) return 0x0646;

	     /* waw / vav */
        if (iransys == 0xF8) return 0x0648;

	     /* heh */
        if (iransys >= 0xF9 && iransys <= 0xFB) return 0x0647;

	     /* farsi yeh */
//	     if (iransys >= 0xFC && iransys <= 0xFE) return 0x06CC;
        if (iransys >= 0xFC && iransys <= 0xFE) return 0x064A;

	     /* no break space */
        if (iransys == 0xFF) return 0xA0;

        return iransys;

    }
}