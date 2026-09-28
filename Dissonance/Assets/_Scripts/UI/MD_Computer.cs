using System;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UIElements;

public class MD_Computer : MonoBehaviour, IPuzzleInteractable, IDataPersistence
{
    [SerializeField] UIDocument computerDoc;
    [SerializeField] string password;
    
    private VisualElement root;
    [NonSerialized] public VisualElement parent;
    
    // FIX 1: Initialize the lists so they aren't null
    [NonSerialized] private List<string> stringTitles = new List<string>();
    [NonSerialized] private List<string> stringContents = new List<string>();
    
    [SerializeField] private MedicalWardKeyItemID keyItemID1;
    [SerializeField] private MedicalWardKeyItemID keyItemID2;

    private void Awake() 
    {
        if (computerDoc != null)
        {
            root = computerDoc.rootVisualElement;
        }
    }

    private void Start() 
    {
        if (root == null) return;

        parent = root.Q<VisualElement>("parent");
        
        root.Query(className: "exit").ForEach(el => {
            el.RegisterCallback<ClickEvent>(e =>
            {
                parent.style.display = DisplayStyle.None;
            });
        });
        
        root.Q<VisualElement>("enter").RegisterCallback<ClickEvent>(e =>
        {
            if(root.Q<TextField>("password-field").text.Equals(password))
            {
                root.Q<VisualElement>("popup-parent").style.display = DisplayStyle.None;
            }
        });

        RefreshContent();
    }

    public void AddContent(List<string> titles, List<string> contents)
    {
        foreach(string s in titles)
            stringTitles.Add(s);
        foreach(string s in contents)
            stringContents.Add(s);

        //this.stringTitles = titles;
        //this.stringContents = contents;
        RefreshContent();
    }

    public void AddContent(string title, string content)
    {
        this.stringTitles.Add(title);
        this.stringContents.Add(content);
        RefreshContent();
    }

    private void RefreshContent()
    {
        if (root == null) return;

        VisualElement noteParent = root.Q<VisualElement>("notes");
        if (noteParent == null) return;

        noteParent.Clear();

        Label textTitleEl = root.Q<Label>("title");
        Label textContentEl = root.Q<Label>("content");

        if (stringTitles.Count > 0)
        {
            if (textTitleEl != null) textTitleEl.text = stringTitles[0];
            if (textContentEl != null) textContentEl.text = stringContents[0];
        }

        for (int i = 0; i < stringTitles.Count; i++)
        {
            int index = i;
            string title = stringTitles[i];

            VisualElement titleEl = new VisualElement();
            titleEl.AddToClassList("note-bar");

            Label textEl = new Label(title);
            textEl.AddToClassList("note-bar-text");
            
            titleEl.Add(textEl);

            titleEl.RegisterCallback<ClickEvent>(e =>
            {
                if (textTitleEl != null) textTitleEl.text = stringTitles[index];
                if (textContentEl != null) textContentEl.text = stringContents[index];
                Debug.Log($"Viewing note: {stringTitles[index]}");
            });

            noteParent.Add(titleEl);
        }
    }

    public bool CheckKeyItem<T>(T keyID) where T : System.Enum
    {
        return keyID.Equals(keyItemID1)||keyID.Equals(keyItemID2);
    }

    public void OpenPuzzle()
    {
        root.Q<VisualElement>("popup-parent").style.display = DisplayStyle.Flex;
        parent.style.display = DisplayStyle.Flex;
    }

    public void LoadData(GameData data)
    {
        if (data.MDComputerContents == null) return;

        foreach(KeyValuePair<string, string> valuePair in data.MDComputerContents)
        {
            if (!stringTitles.Contains(valuePair.Key)) 
            {
                AddContent(valuePair.Key, valuePair.Value);
            }
        }
    }

    public void SaveData(ref GameData data)
    {
        for(int i = 0; i < stringTitles.Count; i++)
        {
            data.MDComputerContents[stringTitles[i]] = stringContents[i];
        }
    }
}