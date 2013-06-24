package org.freeinternals.format.pdf.basicobj;

import java.io.IOException;
import org.freeinternals.commonlib.core.FileComponent;
import org.freeinternals.commonlib.core.PosDataInputStream;
import org.freeinternals.commonlib.util.DefaultFileComponent;
import org.freeinternals.format.pdf.PDFStatics;
import org.freeinternals.format.pdf.Texts;

/**
 * Basic object analysis.
 *
 * @author Amos Shi
 */
public class Analysis {

    public FileComponent ParseNextObject(PosDataInputStream stream) throws IOException {
        FileComponent comp;
        byte next1 = stream.readByte();
        byte next2;
//        System.out.println(String.format("Analysis.ParseNextObject(): location = %d (0x%X), next1 = '%c'",
//                stream.getPos(),
//                stream.getPos(),
//                (char) next1));
        switch (next1) {
            case PDFStatics.WhiteSpace.LF:                                      // New Line
            case PDFStatics.WhiteSpace.CR:                                      // New Line
                comp = new DefaultFileComponent(stream.getPos() - 1, 1, Texts.NewLine);
                break;
            case PDFStatics.WhiteSpace.SP:                                      // ' '  - White Space
                comp = new DefaultFileComponent(stream.getPos() - 1, 1, Texts.Space);
                break;
            case PDFStatics.DelimiterCharacter.LP:                              //  '('  - Leteral String
                stream.backward(1);
                comp = new StringLiteral(stream);
                break;
            case PDFStatics.DelimiterCharacter.LT:
                next2 = stream.readByte();
                stream.backward(2);
                if (next2 == PDFStatics.DelimiterCharacter.LT) {                //  '<<' - Dictionary
                    comp = new Dictionary(stream);
                } else {
                    comp = new StringHexadecimal(stream);                       //  '<'  - Hexadecimal String
                }
                break;
            case PDFStatics.DelimiterCharacter.SO:                              //  '/' - Name
                stream.backward(1);
                comp = new Name(stream);
                break;
            case PDFStatics.DelimiterCharacter.LS:                              //  '[' - Array
                stream.backward(1);
                comp = new Array(stream);
                break;
            case Numeric.StartByte.SIGN_ADD:
            case Numeric.StartByte.SIGN_MINUS:
            case Numeric.StartByte.NUMBER_0:
            case Numeric.StartByte.NUMBER_1:
            case Numeric.StartByte.NUMBER_2:
            case Numeric.StartByte.NUMBER_3:
            case Numeric.StartByte.NUMBER_4:
            case Numeric.StartByte.NUMBER_5:
            case Numeric.StartByte.NUMBER_6:
            case Numeric.StartByte.NUMBER_7:
            case Numeric.StartByte.NUMBER_8:
            case Numeric.StartByte.NUMBER_9:
                stream.backward(1);
                comp = new Numeric(stream);
                break;
            case Boolean.StartByte.TRUE:
            case Boolean.StartByte.FALSE:
                stream.backward(1);
                comp = new Boolean(stream);                                     // 'true' 'false' - Boolean Object
                break;
            case Null.StartByte.NULL:
                stream.backward(1);
                comp = new Null(stream);                                        // 'null' - Null Object
                break;
            case Reference.SIGNATURE:
                stream.backward(1);
                comp = new Reference(stream);                                   // '9 0 R' - Reference Indirect Object
                break;
            default:                                                            // Unknown
                stream.backward(1);
                comp = null;
                break;
        } // End Switch

        return comp;
    }
}
