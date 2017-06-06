package com.google.tagmanager;

import com.google.analytics.containertag.proto.Debug.ResolvedFunctionCall;
import com.google.analytics.containertag.proto.Debug.ResolvedProperty;
import com.google.analytics.containertag.proto.Debug.ResolvedRule;
import com.google.analytics.midtier.proto.containertag.TypeSystem.Value;
import com.google.tagmanager.ResourceUtil.ExpandedFunctionCall;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

class DebugResolvedRuleBuilder implements ResolvedRuleBuilder {
    ResolvedFunctionCallTranslatorList addMacrosHolder = new DebugResolvedFunctionCallListTranslator(1);
    ResolvedFunctionCallTranslatorList addTagsHolder = new DebugResolvedFunctionCallListTranslator(3);
    ResolvedFunctionCallTranslatorList removeMacrosHolder = new DebugResolvedFunctionCallListTranslator(2);
    ResolvedFunctionCallTranslatorList removeTagsHolder = new DebugResolvedFunctionCallListTranslator(4);
    ResolvedRule resolvedRule;

    class DebugResolvedFunctionCallListTranslator implements ResolvedFunctionCallTranslatorList {
        private final int type;

        class Type {
            static final int ADD_MACROS = 1;
            static final int ADD_TAGS = 3;
            static final int REMOVE_MACROS = 2;
            static final int REMOVE_TAGS = 4;

            Type() {
            }
        }

        DebugResolvedFunctionCallListTranslator(int type) {
            this.type = type;
        }

        public void translateAndAddAll(List<ExpandedFunctionCall> functions, List<String> ruleNames) {
            List<ResolvedFunctionCall> translatedList = new ArrayList(functions.size());
            for (int i = 0; i < functions.size(); i++) {
                translatedList.add(DebugResolvedRuleBuilder.translateExpandedFunctionCall((ExpandedFunctionCall) functions.get(i)));
                if (i < ruleNames.size()) {
                    ((ResolvedFunctionCall) translatedList.get(i)).associatedRuleName = (String) ruleNames.get(i);
                } else {
                    ((ResolvedFunctionCall) translatedList.get(i)).associatedRuleName = "Unknown";
                }
            }
            ResolvedFunctionCall[] translated = (ResolvedFunctionCall[]) translatedList.toArray(new ResolvedFunctionCall[0]);
            switch (this.type) {
                case 1:
                    DebugResolvedRuleBuilder.this.resolvedRule.addMacros = translated;
                    return;
                case 2:
                    DebugResolvedRuleBuilder.this.resolvedRule.removeMacros = translated;
                    return;
                case 3:
                    DebugResolvedRuleBuilder.this.resolvedRule.addTags = translated;
                    return;
                case 4:
                    DebugResolvedRuleBuilder.this.resolvedRule.removeTags = translated;
                    return;
                default:
                    Log.e("unknown type in translateAndAddAll: " + this.type);
                    return;
            }
        }
    }

    public DebugResolvedRuleBuilder(ResolvedRule rule) {
        this.resolvedRule = rule;
    }

    public ResolvedFunctionCallBuilder createNegativePredicate() {
        ResolvedFunctionCall predicate = new ResolvedFunctionCall();
        this.resolvedRule.negativePredicates = ArrayUtils.appendToArray(this.resolvedRule.negativePredicates, predicate);
        return new DebugResolvedFunctionCallBuilder(predicate);
    }

    public ResolvedFunctionCallBuilder createPositivePredicate() {
        ResolvedFunctionCall predicate = new ResolvedFunctionCall();
        this.resolvedRule.positivePredicates = ArrayUtils.appendToArray(this.resolvedRule.positivePredicates, predicate);
        return new DebugResolvedFunctionCallBuilder(predicate);
    }

    public ResolvedFunctionCallTranslatorList getAddedMacroFunctions() {
        return this.addMacrosHolder;
    }

    public ResolvedFunctionCallTranslatorList getRemovedMacroFunctions() {
        return this.removeMacrosHolder;
    }

    public ResolvedFunctionCallTranslatorList getAddedTagFunctions() {
        return this.addTagsHolder;
    }

    public ResolvedFunctionCallTranslatorList getRemovedTagFunctions() {
        return this.removeTagsHolder;
    }

    public void setValue(Value result) {
        this.resolvedRule.result = DebugValueBuilder.copyImmutableValue(result);
    }

    public static ResolvedFunctionCall translateExpandedFunctionCall(ExpandedFunctionCall f) {
        ResolvedFunctionCall result = new ResolvedFunctionCall();
        for (Entry<String, Value> originalParam : f.getProperties().entrySet()) {
            ResolvedProperty prop = new ResolvedProperty();
            prop.key = (String) originalParam.getKey();
            prop.value = DebugValueBuilder.copyImmutableValue((Value) originalParam.getValue());
            result.properties = ArrayUtils.appendToArray(result.properties, prop);
        }
        return result;
    }
}
