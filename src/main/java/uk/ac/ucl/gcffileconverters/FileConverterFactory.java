package uk.ac.ucl.gcffileconverters;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author parvez
 */
public class FileConverterFactory {

	public Fileconverters createFileConverter(int type) {

		// System.out.println("Type = "+type);
		Fileconverters fc = null;

		if (type == IConstant.NICAD_RESULT_FILE) {
			fc = new FileConverterForNicad();
		} else if (type == IConstant.CCFINDER_RESULT_FILE) {
			fc = new FileConverterForCCFinder();
		} else if (type == IConstant.RCF_RESULT_FILE) {
			fc = new FileConverterForRCF();
		} else if (type == IConstant.SIIMIAN_RESULT_FILE) {
			fc = new FileConverterForSimian();
		} else if (type == IConstant.SIMSCAN_RESULT_FILE) {
			fc = new FileConverterForSimscan();
		} else if (type == IConstant.VISCAD_INPUT_FILE) {
			fc = new FileConverterForViscadInputFile();
		} else if (type == IConstant.CPD_RESULT_FILE) {
			fc = new FileConverterForCPD();
		} else if (type == IConstant.CONQAT_RESULT_FILE) {
			fc = new FileConverterForConQAT();
		} else if (type == IConstant.DECKARD_RESULT_FILE) {
			fc = new FileConverterForDeckard();
		} else if (type == IConstant.SOURCERERCC_RESULT_FILE) {
			fc = new FileConverterForSCC();
		} else if (type == IConstant.CCALIGNER_RESULT_FILE) {
			fc = new FileConverterForCCAligner();
		}

		return fc;
	}
}
