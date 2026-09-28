/*using UnityEngine;
using UnityEditor;

[CustomEditor(typeof(Item),true)]
public class ItemEditor : Editor
{
    // Properties to handle
    SerializedProperty itemObjectProp;
    SerializedProperty itemtypeProp;
    SerializedProperty keyNameProp;

    void OnEnable()
    {
        // Link the variables
        itemObjectProp = serializedObject.FindProperty("itemObject");
        itemtypeProp = serializedObject.FindProperty("itemtype");
        keyNameProp = serializedObject.FindProperty("keyName");
    }

    public override void OnInspectorGUI()
    {
        // Update the serialized object (essential!)
        serializedObject.Update();

        // 1. Draw the standard fields you always want
        EditorGUILayout.PropertyField(itemObjectProp);
        EditorGUILayout.PropertyField(itemtypeProp);

        // 2. Check the Enum value
        // We get the index of the enum (Default=0, Key=1, etc.)
        Itemtype currentType = (Itemtype)itemtypeProp.enumValueIndex;

        // 3. Conditional Logic: Only draw keyName if type is Key
        if (currentType == Itemtype.Key)
        {
            EditorGUILayout.Space(); // Add a little visual gap
            EditorGUILayout.PropertyField(keyNameProp);
        }

        // Apply changes to the actual object
        serializedObject.ApplyModifiedProperties();
    }
}*/