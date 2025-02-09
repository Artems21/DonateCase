package com.jodexindustries.donatecase.common.serializer;

import com.jodexindustries.donatecase.api.DCAPI;
import com.jodexindustries.donatecase.api.data.casedata.CaseDataMaterial;
import com.jodexindustries.donatecase.api.tools.DCTools;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.List;

public class CaseDataMaterialSerializer implements TypeSerializer<CaseDataMaterial> {

    @Override
    public CaseDataMaterial deserialize(Type type, ConfigurationNode node) throws SerializationException {
        CaseDataMaterial material = new CaseDataMaterial();

        material.setId(node.node("ID").getString());
        material.setDisplayName(DCTools.rc(node.node("DisplayName").getString()));
        material.setEnchanted(node.node("Enchanted").getBoolean());
        material.setLore(DCTools.rc(node.node("Lore").getList(String.class)));
        material.setModelData(node.node("ModelData").getInt(-1));

        List<String> rgb = node.node("Rgb").getList(String.class);
        if(rgb != null) material.setRgb(rgb.toArray(new String[0]));

        material.setItemStack(DCAPI.getInstance().getPlatform().getTools().loadCaseItem(material.getId()));

        return material;
    }

    @Override
    public void serialize(Type type, CaseDataMaterial obj, ConfigurationNode node) {
//        if(obj == null) return;
//
//        node.node("ID").set(obj.getId());
//        node.node("DisplayName").set(obj.getDisplayName());
//        node.node("Enchanted").set(obj.isEnchanted());
//        node.node("Lore").set(obj.getLore());
//        node.node("ModelData").set(obj.getModelData());
//        node.node("Rgb").set(obj.getRgb());
    }

    @Override
    public @Nullable CaseDataMaterial emptyValue(Type specificType, ConfigurationOptions options) {
        return new CaseDataMaterial();
    }
}
