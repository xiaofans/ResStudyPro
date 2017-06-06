package org.jsoup.parser;

import android.support.v4.internal.view.SupportMenu;
import com.douban.book.reader.constant.Char;
import com.j256.ormlite.stmt.query.SimpleComparison;
import java.util.Arrays;

enum TokeniserState {
    Data {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.emit(r.consume());
                    return;
                case '&':
                    t.advanceTransition(CharacterReferenceInData);
                    return;
                case '<':
                    t.advanceTransition(TagOpen);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.emit(new EOF());
                    return;
                default:
                    t.emit(r.consumeData());
                    return;
            }
        }
    },
    CharacterReferenceInData {
        void read(Tokeniser t, CharacterReader r) {
            char[] c = t.consumeCharacterReference(null, false);
            if (c == null) {
                t.emit('&');
            } else {
                t.emit(c);
            }
            t.transition(Data);
        }
    },
    Rcdata {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '&':
                    t.advanceTransition(CharacterReferenceInRcdata);
                    return;
                case '<':
                    t.advanceTransition(RcdataLessthanSign);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.emit(new EOF());
                    return;
                default:
                    t.emit(r.consumeToAny('&', '<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    CharacterReferenceInRcdata {
        void read(Tokeniser t, CharacterReader r) {
            char[] c = t.consumeCharacterReference(null, false);
            if (c == null) {
                t.emit('&');
            } else {
                t.emit(c);
            }
            t.transition(Rcdata);
        }
    },
    Rawtext {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '<':
                    t.advanceTransition(RawtextLessthanSign);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.emit(new EOF());
                    return;
                default:
                    t.emit(r.consumeToAny('<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    ScriptData {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '<':
                    t.advanceTransition(ScriptDataLessthanSign);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.emit(new EOF());
                    return;
                default:
                    t.emit(r.consumeToAny('<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    PLAINTEXT {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.emit(new EOF());
                    return;
                default:
                    t.emit(r.consumeTo((char) TokeniserState.nullChar));
                    return;
            }
        }
    },
    TagOpen {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case '!':
                    t.advanceTransition(MarkupDeclarationOpen);
                    return;
                case '/':
                    t.advanceTransition(EndTagOpen);
                    return;
                case '?':
                    t.advanceTransition(BogusComment);
                    return;
                default:
                    if (r.matchesLetter()) {
                        t.createTagPending(true);
                        t.transition(TagName);
                        return;
                    }
                    t.error((TokeniserState) this);
                    t.emit('<');
                    t.transition(Data);
                    return;
            }
        }
    },
    EndTagOpen {
        void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.emit("</");
                t.transition(Data);
            } else if (r.matchesLetter()) {
                t.createTagPending(false);
                t.transition(TagName);
            } else if (r.matches('>')) {
                t.error((TokeniserState) this);
                t.advanceTransition(Data);
            } else {
                t.error((TokeniserState) this);
                t.advanceTransition(BogusComment);
            }
        }
    },
    TagName {
        void read(Tokeniser t, CharacterReader r) {
            t.tagPending.appendTagName(r.consumeTagName().toLowerCase());
            switch (r.consume()) {
                case '\u0000':
                    t.tagPending.appendTagName(TokeniserState.replacementStr);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeAttributeName);
                    return;
                case '/':
                    t.transition(SelfClosingStartTag);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    RcdataLessthanSign {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matches((char) Char.SLASH)) {
                t.createTempBuffer();
                t.advanceTransition(RCDATAEndTagOpen);
            } else if (!r.matchesLetter() || t.appropriateEndTagName() == null || r.containsIgnoreCase("</" + t.appropriateEndTagName())) {
                t.emit(SimpleComparison.LESS_THAN_OPERATION);
                t.transition(Rcdata);
            } else {
                t.tagPending = t.createTagPending(false).name(t.appropriateEndTagName());
                t.emitTagPending();
                r.unconsume();
                t.transition(Data);
            }
        }
    },
    RCDATAEndTagOpen {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.tagPending.appendTagName(Character.toLowerCase(r.current()));
                t.dataBuffer.append(Character.toLowerCase(r.current()));
                t.advanceTransition(RCDATAEndTagName);
                return;
            }
            t.emit("</");
            t.transition(Rcdata);
        }
    },
    RCDATAEndTagName {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
                return;
            }
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    if (t.isAppropriateEndTagToken()) {
                        t.transition(BeforeAttributeName);
                        return;
                    } else {
                        anythingElse(t, r);
                        return;
                    }
                case '/':
                    if (t.isAppropriateEndTagToken()) {
                        t.transition(SelfClosingStartTag);
                        return;
                    } else {
                        anythingElse(t, r);
                        return;
                    }
                case '>':
                    if (t.isAppropriateEndTagToken()) {
                        t.emitTagPending();
                        t.transition(Data);
                        return;
                    }
                    anythingElse(t, r);
                    return;
                default:
                    anythingElse(t, r);
                    return;
            }
        }

        private void anythingElse(Tokeniser t, CharacterReader r) {
            t.emit("</" + t.dataBuffer.toString());
            r.unconsume();
            t.transition(Rcdata);
        }
    },
    RawtextLessthanSign {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matches((char) Char.SLASH)) {
                t.createTempBuffer();
                t.advanceTransition(RawtextEndTagOpen);
                return;
            }
            t.emit('<');
            t.transition(Rawtext);
        }
    },
    RawtextEndTagOpen {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.transition(RawtextEndTagName);
                return;
            }
            t.emit("</");
            t.transition(Rawtext);
        }
    },
    RawtextEndTagName {
        void read(Tokeniser t, CharacterReader r) {
            TokeniserState.handleDataEndTag(t, r, Rawtext);
        }
    },
    ScriptDataLessthanSign {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '!':
                    t.emit("<!");
                    t.transition(ScriptDataEscapeStart);
                    return;
                case '/':
                    t.createTempBuffer();
                    t.transition(ScriptDataEndTagOpen);
                    return;
                default:
                    t.emit(SimpleComparison.LESS_THAN_OPERATION);
                    r.unconsume();
                    t.transition(ScriptData);
                    return;
            }
        }
    },
    ScriptDataEndTagOpen {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.transition(ScriptDataEndTagName);
                return;
            }
            t.emit("</");
            t.transition(ScriptData);
        }
    },
    ScriptDataEndTagName {
        void read(Tokeniser t, CharacterReader r) {
            TokeniserState.handleDataEndTag(t, r, ScriptData);
        }
    },
    ScriptDataEscapeStart {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matches((char) Char.HYPHEN)) {
                t.emit((char) Char.HYPHEN);
                t.advanceTransition(ScriptDataEscapeStartDash);
                return;
            }
            t.transition(ScriptData);
        }
    },
    ScriptDataEscapeStartDash {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matches((char) Char.HYPHEN)) {
                t.emit((char) Char.HYPHEN);
                t.advanceTransition(ScriptDataEscapedDashDash);
                return;
            }
            t.transition(ScriptData);
        }
    },
    ScriptDataEscaped {
        void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(Data);
                return;
            }
            switch (r.current()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '-':
                    t.emit((char) Char.HYPHEN);
                    t.advanceTransition(ScriptDataEscapedDash);
                    return;
                case '<':
                    t.advanceTransition(ScriptDataEscapedLessthanSign);
                    return;
                default:
                    t.emit(r.consumeToAny(Char.HYPHEN, '<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    ScriptDataEscapedDash {
        void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(Data);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.emit((char) TokeniserState.replacementChar);
                    t.transition(ScriptDataEscaped);
                    return;
                case '-':
                    t.emit(c);
                    t.transition(ScriptDataEscapedDashDash);
                    return;
                case '<':
                    t.transition(ScriptDataEscapedLessthanSign);
                    return;
                default:
                    t.emit(c);
                    t.transition(ScriptDataEscaped);
                    return;
            }
        }
    },
    ScriptDataEscapedDashDash {
        void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(Data);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.emit((char) TokeniserState.replacementChar);
                    t.transition(ScriptDataEscaped);
                    return;
                case '-':
                    t.emit(c);
                    return;
                case '<':
                    t.transition(ScriptDataEscapedLessthanSign);
                    return;
                case '>':
                    t.emit(c);
                    t.transition(ScriptData);
                    return;
                default:
                    t.emit(c);
                    t.transition(ScriptDataEscaped);
                    return;
            }
        }
    },
    ScriptDataEscapedLessthanSign {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTempBuffer();
                t.dataBuffer.append(Character.toLowerCase(r.current()));
                t.emit(SimpleComparison.LESS_THAN_OPERATION + r.current());
                t.advanceTransition(ScriptDataDoubleEscapeStart);
            } else if (r.matches((char) Char.SLASH)) {
                t.createTempBuffer();
                t.advanceTransition(ScriptDataEscapedEndTagOpen);
            } else {
                t.emit('<');
                t.transition(ScriptDataEscaped);
            }
        }
    },
    ScriptDataEscapedEndTagOpen {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.tagPending.appendTagName(Character.toLowerCase(r.current()));
                t.dataBuffer.append(r.current());
                t.advanceTransition(ScriptDataEscapedEndTagName);
                return;
            }
            t.emit("</");
            t.transition(ScriptDataEscaped);
        }
    },
    ScriptDataEscapedEndTagName {
        void read(Tokeniser t, CharacterReader r) {
            TokeniserState.handleDataEndTag(t, r, ScriptDataEscaped);
        }
    },
    ScriptDataDoubleEscapeStart {
        void read(Tokeniser t, CharacterReader r) {
            TokeniserState.handleDataDoubleEscapeTag(t, r, ScriptDataDoubleEscaped, ScriptDataEscaped);
        }
    },
    ScriptDataDoubleEscaped {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.current();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    r.advance();
                    t.emit((char) TokeniserState.replacementChar);
                    return;
                case '-':
                    t.emit(c);
                    t.advanceTransition(ScriptDataDoubleEscapedDash);
                    return;
                case '<':
                    t.emit(c);
                    t.advanceTransition(ScriptDataDoubleEscapedLessthanSign);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.emit(r.consumeToAny(Char.HYPHEN, '<', TokeniserState.nullChar));
                    return;
            }
        }
    },
    ScriptDataDoubleEscapedDash {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.emit((char) TokeniserState.replacementChar);
                    t.transition(ScriptDataDoubleEscaped);
                    return;
                case '-':
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscapedDashDash);
                    return;
                case '<':
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscapedLessthanSign);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscaped);
                    return;
            }
        }
    },
    ScriptDataDoubleEscapedDashDash {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.emit((char) TokeniserState.replacementChar);
                    t.transition(ScriptDataDoubleEscaped);
                    return;
                case '-':
                    t.emit(c);
                    return;
                case '<':
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscapedLessthanSign);
                    return;
                case '>':
                    t.emit(c);
                    t.transition(ScriptData);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.emit(c);
                    t.transition(ScriptDataDoubleEscaped);
                    return;
            }
        }
    },
    ScriptDataDoubleEscapedLessthanSign {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matches((char) Char.SLASH)) {
                t.emit((char) Char.SLASH);
                t.createTempBuffer();
                t.advanceTransition(ScriptDataDoubleEscapeEnd);
                return;
            }
            t.transition(ScriptDataDoubleEscaped);
        }
    },
    ScriptDataDoubleEscapeEnd {
        void read(Tokeniser t, CharacterReader r) {
            TokeniserState.handleDataDoubleEscapeTag(t, r, ScriptDataEscaped, ScriptDataDoubleEscaped);
        }
    },
    BeforeAttributeName {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(AttributeName);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                case '\'':
                case '<':
                case '=':
                    t.error((TokeniserState) this);
                    t.tagPending.newAttribute();
                    t.tagPending.appendAttributeName(c);
                    t.transition(AttributeName);
                    return;
                case '/':
                    t.transition(SelfClosingStartTag);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(AttributeName);
                    return;
            }
        }
    },
    AttributeName {
        void read(Tokeniser t, CharacterReader r) {
            t.tagPending.appendAttributeName(r.consumeToAnySorted(TokeniserState.attributeNameCharsSorted).toLowerCase());
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeName((char) TokeniserState.replacementChar);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(AfterAttributeName);
                    return;
                case '\"':
                case '\'':
                case '<':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeName(c);
                    return;
                case '/':
                    t.transition(SelfClosingStartTag);
                    return;
                case '=':
                    t.transition(BeforeAttributeValue);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AfterAttributeName {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeName((char) TokeniserState.replacementChar);
                    t.transition(AttributeName);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                case '\'':
                case '<':
                    t.error((TokeniserState) this);
                    t.tagPending.newAttribute();
                    t.tagPending.appendAttributeName(c);
                    t.transition(AttributeName);
                    return;
                case '/':
                    t.transition(SelfClosingStartTag);
                    return;
                case '=':
                    t.transition(BeforeAttributeValue);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(AttributeName);
                    return;
            }
        }
    },
    BeforeAttributeValue {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    t.transition(AttributeValue_unquoted);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                    t.transition(AttributeValue_doubleQuoted);
                    return;
                case '&':
                    r.unconsume();
                    t.transition(AttributeValue_unquoted);
                    return;
                case '\'':
                    t.transition(AttributeValue_singleQuoted);
                    return;
                case '<':
                case '=':
                case '`':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue(c);
                    t.transition(AttributeValue_unquoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                default:
                    r.unconsume();
                    t.transition(AttributeValue_unquoted);
                    return;
            }
        }
    },
    AttributeValue_doubleQuoted {
        void read(Tokeniser t, CharacterReader r) {
            String value = r.consumeToAnySorted(TokeniserState.attributeDoubleValueCharsSorted);
            if (value.length() > 0) {
                t.tagPending.appendAttributeValue(value);
            } else {
                t.tagPending.setEmptyAttributeValue();
            }
            switch (r.consume()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    return;
                case '\"':
                    t.transition(AfterAttributeValue_quoted);
                    return;
                case '&':
                    char[] ref = t.consumeCharacterReference(Character.valueOf('\"'), true);
                    if (ref != null) {
                        t.tagPending.appendAttributeValue(ref);
                        return;
                    } else {
                        t.tagPending.appendAttributeValue('&');
                        return;
                    }
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AttributeValue_singleQuoted {
        void read(Tokeniser t, CharacterReader r) {
            String value = r.consumeToAnySorted(TokeniserState.attributeSingleValueCharsSorted);
            if (value.length() > 0) {
                t.tagPending.appendAttributeValue(value);
            } else {
                t.tagPending.setEmptyAttributeValue();
            }
            switch (r.consume()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    return;
                case '&':
                    char[] ref = t.consumeCharacterReference(Character.valueOf('\''), true);
                    if (ref != null) {
                        t.tagPending.appendAttributeValue(ref);
                        return;
                    } else {
                        t.tagPending.appendAttributeValue('&');
                        return;
                    }
                case '\'':
                    t.transition(AfterAttributeValue_quoted);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AttributeValue_unquoted {
        void read(Tokeniser t, CharacterReader r) {
            String value = r.consumeToAny('\t', '\n', Char.CARRIAGE_RETURN, '\f', Char.SPACE, '&', '>', TokeniserState.nullChar, '\"', '\'', '<', '=', '`');
            if (value.length() > 0) {
                t.tagPending.appendAttributeValue(value);
            }
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeAttributeName);
                    return;
                case '\"':
                case '\'':
                case '<':
                case '=':
                case '`':
                    t.error((TokeniserState) this);
                    t.tagPending.appendAttributeValue(c);
                    return;
                case '&':
                    char[] ref = t.consumeCharacterReference(Character.valueOf('>'), true);
                    if (ref != null) {
                        t.tagPending.appendAttributeValue(ref);
                        return;
                    } else {
                        t.tagPending.appendAttributeValue('&');
                        return;
                    }
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AfterAttributeValue_quoted {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeAttributeName);
                    return;
                case '/':
                    t.transition(SelfClosingStartTag);
                    return;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    r.unconsume();
                    t.transition(BeforeAttributeName);
                    return;
            }
        }
    },
    SelfClosingStartTag {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '>':
                    t.tagPending.selfClosing = true;
                    t.emitTagPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.transition(BeforeAttributeName);
                    return;
            }
        }
    },
    BogusComment {
        void read(Tokeniser t, CharacterReader r) {
            r.unconsume();
            Token comment = new Comment();
            comment.bogus = true;
            comment.data.append(r.consumeTo('>'));
            t.emit(comment);
            t.advanceTransition(Data);
        }
    },
    MarkupDeclarationOpen {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchConsume("--")) {
                t.createCommentPending();
                t.transition(CommentStart);
            } else if (r.matchConsumeIgnoreCase("DOCTYPE")) {
                t.transition(Doctype);
            } else if (r.matchConsume("[CDATA[")) {
                t.transition(CdataSection);
            } else {
                t.error((TokeniserState) this);
                t.advanceTransition(BogusComment);
            }
        }
    },
    CommentStart {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '-':
                    t.transition(CommentStartDash);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    CommentStartDash {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '-':
                    t.transition(CommentStartDash);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    Comment {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.current()) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    r.advance();
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    return;
                case '-':
                    t.advanceTransition(CommentEndDash);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append(r.consumeToAny(Char.HYPHEN, TokeniserState.nullChar));
                    return;
            }
        }
    },
    CommentEndDash {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.commentPending.data.append(Char.HYPHEN).append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '-':
                    t.transition(CommentEnd);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append(Char.HYPHEN).append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    CommentEnd {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.commentPending.data.append("--").append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '!':
                    t.error((TokeniserState) this);
                    t.transition(CommentEndBang);
                    return;
                case '-':
                    t.error((TokeniserState) this);
                    t.commentPending.data.append(Char.HYPHEN);
                    return;
                case '>':
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.commentPending.data.append("--").append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    CommentEndBang {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.commentPending.data.append("--!").append(TokeniserState.replacementChar);
                    t.transition(Comment);
                    return;
                case '-':
                    t.commentPending.data.append("--!");
                    t.transition(CommentEndDash);
                    return;
                case '>':
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(Data);
                    return;
                default:
                    t.commentPending.data.append("--!").append(c);
                    t.transition(Comment);
                    return;
            }
        }
    },
    Doctype {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeDoctypeName);
                    return;
                case '>':
                    break;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    break;
                default:
                    t.error((TokeniserState) this);
                    t.transition(BeforeDoctypeName);
                    return;
            }
            t.error((TokeniserState) this);
            t.createDoctypePending();
            t.doctypePending.forceQuirks = true;
            t.emitDoctypePending();
            t.transition(Data);
        }
    },
    BeforeDoctypeName {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.createDoctypePending();
                t.transition(DoctypeName);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.createDoctypePending();
                    t.doctypePending.name.append(TokeniserState.replacementChar);
                    t.transition(DoctypeName);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.createDoctypePending();
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.createDoctypePending();
                    t.doctypePending.name.append(c);
                    t.transition(DoctypeName);
                    return;
            }
        }
    },
    DoctypeName {
        void read(Tokeniser t, CharacterReader r) {
            if (r.matchesLetter()) {
                t.doctypePending.name.append(r.consumeLetterSequence().toLowerCase());
                return;
            }
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.doctypePending.name.append(TokeniserState.replacementChar);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(AfterDoctypeName);
                    return;
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.name.append(c);
                    return;
            }
        }
    },
    AfterDoctypeName {
        void read(Tokeniser t, CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.doctypePending.forceQuirks = true;
                t.emitDoctypePending();
                t.transition(Data);
            } else if (r.matchesAny('\t', '\n', Char.CARRIAGE_RETURN, '\f', Char.SPACE)) {
                r.advance();
            } else if (r.matches('>')) {
                t.emitDoctypePending();
                t.advanceTransition(Data);
            } else if (r.matchConsumeIgnoreCase("PUBLIC")) {
                t.transition(AfterDoctypePublicKeyword);
            } else if (r.matchConsumeIgnoreCase("SYSTEM")) {
                t.transition(AfterDoctypeSystemKeyword);
            } else {
                t.error((TokeniserState) this);
                t.doctypePending.forceQuirks = true;
                t.advanceTransition(BogusDoctype);
            }
        }
    },
    AfterDoctypePublicKeyword {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeDoctypePublicIdentifier);
                    return;
                case '\"':
                    t.error((TokeniserState) this);
                    t.transition(DoctypePublicIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.error((TokeniserState) this);
                    t.transition(DoctypePublicIdentifier_singleQuoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    BeforeDoctypePublicIdentifier {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                    t.transition(DoctypePublicIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.transition(DoctypePublicIdentifier_singleQuoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    DoctypePublicIdentifier_doubleQuoted {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.doctypePending.publicIdentifier.append(TokeniserState.replacementChar);
                    return;
                case '\"':
                    t.transition(AfterDoctypePublicIdentifier);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.publicIdentifier.append(c);
                    return;
            }
        }
    },
    DoctypePublicIdentifier_singleQuoted {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.doctypePending.publicIdentifier.append(TokeniserState.replacementChar);
                    return;
                case '\'':
                    t.transition(AfterDoctypePublicIdentifier);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.publicIdentifier.append(c);
                    return;
            }
        }
    },
    AfterDoctypePublicIdentifier {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BetweenDoctypePublicAndSystemIdentifiers);
                    return;
                case '\"':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    BetweenDoctypePublicAndSystemIdentifiers {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    AfterDoctypeSystemKeyword {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeDoctypeSystemIdentifier);
                    return;
                case '\"':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.error((TokeniserState) this);
                    t.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    return;
            }
        }
    },
    BeforeDoctypeSystemIdentifier {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                    t.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case '\'':
                    t.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    DoctypeSystemIdentifier_doubleQuoted {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.doctypePending.systemIdentifier.append(TokeniserState.replacementChar);
                    return;
                case '\"':
                    t.transition(AfterDoctypeSystemIdentifier);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.systemIdentifier.append(c);
                    return;
            }
        }
    },
    DoctypeSystemIdentifier_singleQuoted {
        void read(Tokeniser t, CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\u0000':
                    t.error((TokeniserState) this);
                    t.doctypePending.systemIdentifier.append(TokeniserState.replacementChar);
                    return;
                case '\'':
                    t.transition(AfterDoctypeSystemIdentifier);
                    return;
                case '>':
                    t.error((TokeniserState) this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.doctypePending.systemIdentifier.append(c);
                    return;
            }
        }
    },
    AfterDoctypeSystemIdentifier {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    t.error((TokeniserState) this);
                    t.transition(BogusDoctype);
                    return;
            }
        }
    },
    BogusDoctype {
        void read(Tokeniser t, CharacterReader r) {
            switch (r.consume()) {
                case '>':
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                case SupportMenu.USER_MASK /*65535*/:
                    t.emitDoctypePending();
                    t.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    CdataSection {
        void read(Tokeniser t, CharacterReader r) {
            t.emit(r.consumeTo("]]>"));
            r.matchConsume("]]>");
            t.transition(Data);
        }
    };
    
    private static final char[] attributeDoubleValueCharsSorted = null;
    private static final char[] attributeNameCharsSorted = null;
    private static final char[] attributeSingleValueCharsSorted = null;
    private static final char eof = '￿';
    static final char nullChar = '\u0000';
    private static final char replacementChar = '�';
    private static final String replacementStr = null;

    abstract void read(Tokeniser tokeniser, CharacterReader characterReader);

    static {
        attributeSingleValueCharsSorted = new char[]{'\'', '&', nullChar};
        attributeDoubleValueCharsSorted = new char[]{'\"', '&', nullChar};
        attributeNameCharsSorted = new char[]{'\t', '\n', Char.CARRIAGE_RETURN, '\f', Char.SPACE, Char.SLASH, '=', '>', nullChar, '\"', '\'', '<'};
        replacementStr = String.valueOf(replacementChar);
        Arrays.sort(attributeSingleValueCharsSorted);
        Arrays.sort(attributeDoubleValueCharsSorted);
        Arrays.sort(attributeNameCharsSorted);
    }

    private static void handleDataEndTag(Tokeniser t, CharacterReader r, TokeniserState elseTransition) {
        if (r.matchesLetter()) {
            String name = r.consumeLetterSequence();
            t.tagPending.appendTagName(name.toLowerCase());
            t.dataBuffer.append(name);
            return;
        }
        boolean needsExitTransition = false;
        if (t.isAppropriateEndTagToken() && !r.isEmpty()) {
            char c = r.consume();
            switch (c) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    t.transition(BeforeAttributeName);
                    break;
                case '/':
                    t.transition(SelfClosingStartTag);
                    break;
                case '>':
                    t.emitTagPending();
                    t.transition(Data);
                    break;
                default:
                    t.dataBuffer.append(c);
                    needsExitTransition = true;
                    break;
            }
        }
        needsExitTransition = true;
        if (needsExitTransition) {
            t.emit("</" + t.dataBuffer.toString());
            t.transition(elseTransition);
        }
    }

    private static void handleDataDoubleEscapeTag(Tokeniser t, CharacterReader r, TokeniserState primary, TokeniserState fallback) {
        if (r.matchesLetter()) {
            String name = r.consumeLetterSequence();
            t.dataBuffer.append(name.toLowerCase());
            t.emit(name);
            return;
        }
        char c = r.consume();
        switch (c) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case '/':
            case '>':
                if (t.dataBuffer.toString().equals("script")) {
                    t.transition(primary);
                } else {
                    t.transition(fallback);
                }
                t.emit(c);
                return;
            default:
                r.unconsume();
                t.transition(fallback);
                return;
        }
    }
}
