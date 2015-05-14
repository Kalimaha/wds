if (!window.CMU) {
	
	window.CMU = {
			
		editor : null,
		
		init : function(textAreaID) {
			var mime = 'text/x-mariadb';
			if (window.location.href.indexOf('mime=') > -1) {
				mime = window.location.href.substr(window.location.href.indexOf('mime=') + 5);
			} 
			CMU.editor = CodeMirror.fromTextArea(document.getElementById(textAreaID), {
				mode : mime,
				lineNumbers : true,
				tabSize : 3,
				lineWrapping : true,
				styleActiveLine: true,
				indentWithTabs: true,
				smartIndent: true,
				matchBrackets : true,
				autofocus: true 
			});
		},
		
		getValue : function() {
			return CMU.editor.getValue();
		}
			
	};
	
}