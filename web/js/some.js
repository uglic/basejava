setSectionHandlers(document);
fitTextareasHeight();

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

function addSection(e){
  e.preventDefault();
  if(this.ownerId != undefined){
    var elem = getNearestSection(this.parentNode, this.ownerId);
    if(elem != undefined && elem != null){
       var parent = elem.parentNode;
       var count = 0;
       if(parent.sectionCounter != undefined && parent.sectionCounter != null ) {
         count = Number(parent.sectionCounter) + 1;
       }
       parent.sectionCounter = count;
       var node = elem.cloneNode(true);
       node.id = elem.id + "." + count;
       clearInputFields(node);
       setSectionHandlers(node);
       elem.parentNode.appendChild(node); //beforebegin:afterend
    }
  }
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

function clearInputFields(node){
  clearElementValue(node, "input");
  clearElementValue(node, "textarea");
  clearElementValue(node, "select");
}

function clearElementValue(node, name){
 var elems = node.getElementsByTagName(name);
 for(var i = 0; i < elems.length; i++){elems[i].value = "";}
}

function delSection(e){
  if(this.ownerId == undefined || this.ownerId == null) {return;}
  e.preventDefault();
  var node = getNodeByOwnerId(this, this.ownerId);
  if(node != null && node.parentNode != null){
    if (node.parentNode.querySelectorAll("[id^=" + this.ownerId + "]").length > 1){
      node.parentNode.removeChild(node);
    }
  }
}

function getNodeByOwnerId(from, ownerId){
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