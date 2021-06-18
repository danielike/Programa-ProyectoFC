/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author a
 */
public final class DocumentoLongitudRestringida extends PlainDocument{
    private final int limiteCaracteres;

    public DocumentoLongitudRestringida(int limiteCaracteres) {
        this.limiteCaracteres = limiteCaracteres;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str == null)
            return;
        if((getLength() + str.length()) <= limiteCaracteres){
            super.insertString(offs, str, a);
        }
    }
    
    
}
