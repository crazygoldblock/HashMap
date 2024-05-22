using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

/// <summary>
/// t��da kter� se star� o chat
/// pokud chce kter�koliv jin� t��da zobrazit zpr�vu do chatu mus� zavolat funkce z t�to t��dy
/// </summary>
public class Chat : MonoBehaviour
{
    public int maxMessages = 50;

    public List<GameObject> messages = new List<GameObject>();

    public GameObject chatPanel, textObject, scrollView;
    public InputField chatBox;

    public static Chat chat;

    private bool isEnabled = false;
    private bool enableTime = false;

    Coroutine c = null;

    private void Awake()
    {
        chat = this;
    }
    void Update() {

        if (Input.GetKeyDown(KeyCode.Return))
        {
            if (!isEnabled)
            {
                isEnabled = true;
                EnableChat();
                WriteToChat();
            }
            else
            {
                if (chatBox.text.Trim() != "")
                {
                    string playerName = NetworkGameManager.playerName;
                    SendMessageChat(playerName, chatBox.text.Trim(), false);
                    chatBox.text = "";
                }
                isEnabled = false;
                StopWriteToChat();
                DisableChat();
            }
        }
        if (Input.GetKeyDown(KeyCode.Escape))
        {
            if (isEnabled)
            {
                DisableChat();
                StopWriteToChat();
            }
        }
    }
    public void EnableChat()
    {
        chatBox.gameObject.SetActive(true);
        scrollView.SetActive(true);
    }
    public void WriteToChat()
    {
        chatBox.ActivateInputField();
        Controler.instance.EnableControls(false);

        isEnabled = true;
    }
    public void StopWriteToChat()
    {
        chatBox.DeactivateInputField();
        Controler.instance.EnableControls(true);

        isEnabled = false;
    }
    public void DisableChat()
    {
        if (enableTime)
            return;

        chatBox.gameObject.SetActive(false);
        scrollView.SetActive(false);
    }
    IEnumerator ShowChat()
    {
        enableTime = true;
        EnableChat();

        yield return new WaitForSeconds(6f);

        enableTime = false;
        DisableChat();
    }
    /// <summary>
    /// zobrazit zpr�vu v chatu
    /// pokud system == true tuto zpr�vu NEpo�le ostatn�m client�m po s�ti 
    /// </summary>
    public void SendMessageChat(string username, string text, bool system) {

        if (c != null)
            StopCoroutine(c);
        
        c = StartCoroutine(ShowChat());

        if (messages.Count >= maxMessages)
        {
            Destroy(messages[0]);
            messages.RemoveAt(0);
        }

        GameObject newText = Instantiate(textObject, chatPanel.transform);
        
        newText.GetComponent<Text>().text = username + ": " + text;

        messages.Add(newText);

        if (!system)
            NetworkGameManager.instance.SendChatMessage(username, text);
    }
}