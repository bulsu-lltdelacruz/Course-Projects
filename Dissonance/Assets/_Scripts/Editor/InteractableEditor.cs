/*using UnityEngine;
using UnityEditor;
DELETE
[CustomEditor(typeof(Interactable),true)]
public class InteractableEditor : Editor
{
    // Properties to handle
    SerializedProperty dialogueContentProp;
    SerializedProperty interactableTypeProp;
    SerializedProperty keyItemIDProp;

    void OnEnable()
    {
        // Link the variables
        dialogueContentProp = serializedObject.FindProperty("dialogueContents");
        interactableTypeProp = serializedObject.FindProperty("interactableType");
        keyItemIDProp = serializedObject.FindProperty("keyItemID");
    }

    public override void OnInspectorGUI()
    {
        // Update the serialized object (essential!)
        serializedObject.Update();

        // 1. Draw the standard fields you always want
        EditorGUILayout.PropertyField(dialogueContentProp);
        EditorGUILayout.PropertyField(interactableTypeProp);

        // 2. Check the Enum value
        InteractableType currentType = (InteractableType)interactableTypeProp.enumValueIndex;

        // 3. Conditional Logic: Only draw KeyItemID if type is Locked
        if (currentType == InteractableType.Locked)
        {
            EditorGUILayout.Space(); // Add a little visual gap
            EditorGUILayout.PropertyField(keyItemIDProp);
        }

        // Apply changes to the actual object
        serializedObject.ApplyModifiedProperties();
    }
}*/
