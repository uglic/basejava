resume = new mainResume(document);
resume.start();

function mainResume(context) {
    var document = context;

    function fitTextareasHeight(){
      areas = document.getElementsByTagName("textarea");
      for(var i = 0; i < areas.length; i++){
        var e = areas[i];
        e.style.height = "1px";
        //if (e.scrollHeight > e.clientHeight) {
          e.style.height = e.scrollHeight + getStyleInPixels(e, "borderTopWidth")
             + getStyleInPixels(e, "borderBottomWidth") + "px";
        //}
      }
    }

    function getStyleInPixels(elem, styleJsName){
      var s = window.getComputedStyle(elem)[styleJsName];
      return Number(s.substring(0, s.length - 2));
    }

    function setSectionHandlers(root){
      links = root.getElementsByTagName("a");
      for(var linkId = 0; linkId < links.length; linkId++){
        var e = links[linkId];
        var classes = e.className.split(" ");
        for(var i = 0; i < classes.length; i++){
          var cl = classes[i];
          var prefix = cl.substring(0, 4);
          var postfix = cl.substring(4, cl.length);
          if(prefix == "add:"){
            e.ownerId = postfix;
            e.addEventListener("click", addSection);
          }
          if(prefix == "del:"){
            e.ownerId = postfix;
            e.addEventListener("click", delSection);
          }
        }
      }
    }

    function splitName(name) {
       // Sample name = "EDUCATION-org.1.2":
       // EDUCATION == prefix
       // -org == postfix
       // 1 == section counter
       // 2 == subsection counter

        var prefix = name;
        var postfix = "";
        var numbers = "";
        var section = null;
        var subsection = null;
        var index;

        index = prefix.indexOf(".");
        if(index >= 0) {
            numbers = prefix.substring(index + 1, prefix.length);
            prefix = prefix.substring(0, index);
        }
        index = prefix.indexOf("-");
        if(index >= 0) {
            postfix = prefix.substring(index, prefix.length);
            prefix = prefix.substring(0, index);
        }
        if(numbers.length >= 0 ){
            index = numbers.indexOf(".");
            if(index >= 0){
                section = Number(numbers.substring(0, index));
                numbers = numbers.substring(index + 1, numbers.length);
                subsection = Number(numbers);
            } else {
                section = Number(numbers);
            }
        }
        return {
            prefix: prefix,
            postfix: postfix,
            section: section,
            subsection: subsection
        }
    }

    function delSection(e){
      e.preventDefault();
      if(this.ownerId == undefined || this.ownerId == null) {return;}
      var node = getRootForOwnerId(this, this.ownerId);
      if(node != null && node.parentNode != null){
        if (node.parentNode.querySelectorAll("[id^=" + this.ownerId + "]").length > 1){
          node.parentNode.removeChild(node);
        }
      }
    }

    function addSection(e){
      e.preventDefault();
      if(this.ownerId != undefined){
        var sectionRoot = getNearestSection(this.parentNode, this.ownerId);
        if(sectionRoot != undefined && sectionRoot != null){
           var sections = sectionRoot.parentNode;
           var count = sections.childNodes.length + 1;
           if(sections.sectionCounter != undefined && sections.sectionCounter != null ) {
             count = Number(sections.sectionCounter) + 1;
           }
           sections.sectionCounter = count;

           var np = splitName(sectionRoot.id);
           var newNode = sectionRoot.cloneNode(true);
           newNode.id = np.prefix + np.postfix + ((np.subsection == null)?"":"." + np.section)  + "." + count;

           clearFields(newNode, np.prefix);
           if(np.subsection != null) {
               updateIdToCounters(newNode, np.prefix, np.section, count);
               updateNameToCounters(newNode, np.prefix, np.section, count);
           } else {
               updateIdToCounters(newNode, np.prefix, count, null);
               updateNameToCounters(newNode, np.prefix, count, null);
           }
           setSectionHandlers(newNode);
           sections.appendChild(newNode);
        }
      }
    }

    function updateIdToCounters(from, prefix, section, subsection){
       if(prefix == null || prefix.length == 0) return;
       if(from.querySelectorAll != undefined) {
           var elements = from.querySelectorAll("[id^=" + prefix + "]");
           updateCollectionWithCounters(elements, "id", prefix, section, subsection);
       }
    }

    function updateNameToCounters(from, prefix, section, subsection){
       if(prefix == null || prefix.length == 0) return;
       if(from.querySelectorAll != undefined) {
           var elements;
           elements = from.querySelectorAll("input[name^=" + prefix + "]");
           updateCollectionWithCounters(elements, "name", prefix, section, subsection);

           elements = from.querySelectorAll("textarea[name^=" + prefix + "]");
           updateCollectionWithCounters(elements, "name", prefix, section, subsection);
       }
    }

    function getChangedToCounters(id, section, subsection){
        if(id == null || id.length == 0) return id;
        var np = splitName(id);
        id = np.prefix + np.postfix;
        id += "." + section;
        if(subsection != null) {
            id += "." + subsection;
        } else {
            if(np.subsection != null) id += "." + np.subsection;
        }
        return id;
    }

    function updateCollectionWithCounters(elements, attribute, prefix, section, subsection){
       if(prefix == null || prefix.length == 0 || elements == null) return;
       for(var i = 0; i < elements.length; i++){
            var value = elements[i].getAttribute(attribute);
            elements[i].setAttribute(attribute, getChangedToCounters(value, section, subsection));
       }
    }

    function clearElementsValue(node, name){
        var elements = node.getElementsByTagName(name);
        for(var i = 0; i < elements.length; i++){
            elements[i].value = "";
        }
    }

    function clearFields(node, prefix){
    //   if(node.querySelectorAll != undefined) {
    //       var elems;
    //       elems = from.querySelectorAll("[id^=" + prefix + "]");
    //       for(var i = 0; i < elems.length; i++){
    //            elems[i].name = getChangedToCounters(elems[i].name, section, subsection);
    //       }
    //   }
      clearElementsValue(node, "input");
      clearElementsValue(node, "textarea");
      clearElementsValue(node, "select");
    }

    function getNearestSection(from, ownerId){
      var nodes = from.parentNode.childNodes;
      if(nodes != undefined && nodes != null){
        for(var i = 0; i < nodes.length; i++){
           if(nodes[i].querySelectorAll != undefined) {
             var possible = nodes[i].querySelectorAll("[id^=" + ownerId + "]");
               if(possible.length > 0){
                 return possible[0];
               }
           }
        }
      }
      return null;
    }

    function getRootForOwnerId(from, ownerId){
      var parent = from.parentNode;
      while(parent != undefined && parent != null){
        if(parent.id != undefined && parent.id.startsWith(ownerId)){
          if(parent.id == ownerId  || parent.id.startsWith(ownerId + ".")){
            return parent;
          }
        }
        parent = parent.parentNode;
      }
      return null;
    }

    this.start = function() {
        setSectionHandlers(document);
        fitTextareasHeight();
    }
}