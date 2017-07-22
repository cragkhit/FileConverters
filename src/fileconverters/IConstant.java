package fileconverters;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author parvez
 * @author Chaiyong
 */
public class IConstant {
    public static boolean readClonedCodeOnly =true;
    public static boolean originalSource     =true;
    
    //Constant related with Clone Detection Tool/File format
    public static final int VISCAD_INPUT_FILE = 1001;
    public static final int SIIMIAN_RESULT_FILE = 1002;
    public static final int NICAD_RESULT_FILE = 1003;
    public static final int SIMSCAN_RESULT_FILE = 1004;
    public static final int RCF_RESULT_FILE = 1005;
    public static final int CCFINDER_RESULT_FILE = 1006;
    public static final int CPD_RESULT_FILE = 1007;
    public static final int CONQAT_RESULT_FILE = 1008;
    public static final int DECKARD_RESULT_FILE = 1009;
	public static final int SOURCERERCC_RESULT_FILE = 1010;
    
    //COnstants related with granularity of clone
    public static int GRANULARITY_FUNCTION = 2001;
    public static int GRANULARITY_BLOCK = 2002;
    
  
}
