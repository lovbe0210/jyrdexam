var mainPlatform = {

	init: function(){

		this.bindEvent();
		this._createTopMenu();
	},

	bindEvent: function(){
		var self = this;
		// 顶部大菜单单击事件
		$(document).on('click', '.pf-nav-item', function() {
            $('.pf-nav-item').removeClass('current');
            $(this).addClass('current');

            // 渲染对应侧边菜单
            var m = $(this).data('sort');
            self._createSiderMenu(SystemMenu[m], m);
        });

        $(document).on('click', '.sider-nav .pf-menu-title', function() {
        	$(this).closest('.pf-sider').find('.sider-nav li').removeClass('current');
            $(this).closest('li').addClass('current');

            // if is no-child
            if($(this).closest('.no-child').size() > 0) {
            	var index = $(this).closest('.pf-sider').attr('arrindex');
	        	if($('.easyui-tabs1[arrindex='+ index +']').tabs('exists', $(this).find('.sider-nav-title').text())){
	        		$('.easyui-tabs1[arrindex='+ index +']').tabs('select', $(this).find('.sider-nav-title').text())
	        		return false;
	        	}
	        	$('.easyui-tabs1[arrindex='+ index +']').tabs('add',{
					title: $(this).find('.sider-nav-title').text(),
					content: '<iframe class="page-iframe" src="'+ $(this).closest('.no-child').data('href') +'" frameborder="no" border="no" height="100%" width="100%" scrolling="auto"></iframe>',
					closable: true
				});
            }
            //$('iframe').attr('src', $(this).data('src'));
        });

        $(document).on('click', '.sider-nav-s > li', function(e){
        	var index = $(this).closest('.pf-sider').attr('arrindex');
        	var titleName = $(this).find('a').eq(0).text();
        	var url = $(this).data('href');
        	var $this = $(this);
        	var $target = $(e.target);

        	if($target.closest('ul').hasClass('sider-nav-t')) {
        		return;
        	}

        	$(this).closest('.pf-sider').find('.active').removeClass('active');
    		$(this).addClass('active');

        	if(!url) {

        		$this.toggleClass('no-child');
        		return;

        	}

        	if($('.easyui-tabs1[arrindex='+ index +']').tabs('exists', titleName)){
        		$('.easyui-tabs1[arrindex='+ index +']').tabs('select', titleName)
        		return;
        	}
        	$('.easyui-tabs1[arrindex='+ index +']').tabs('add',{
				title: titleName,
				content: '<iframe class="page-iframe" src="'+ $(this).data('href') +'" frameborder="no" border="no" height="100%" width="100%" scrolling="auto"></iframe>',
				closable: true
			});
        });

        $(document).on('click', '.sider-nav-t > li', function(e){
        	var index = $(this).closest('.pf-sider').attr('arrindex');
        	var titleName = $(this).find('a').eq(0).text();
        	var url = $(this).data('href');
        	var $this = $(this);

        	$(this).closest('.pf-sider').find('.active').removeClass('active');
    		$(this).addClass('active');	

        	if(!url) {

        		return;

        	}

        	if($('.easyui-tabs1[arrindex='+ index +']').tabs('exists', titleName)){
        		$('.easyui-tabs1[arrindex='+ index +']').tabs('select', titleName)
        		$(this).parent().closest('li').addClass('active');
        		return;
        	}
        	$('.easyui-tabs1[arrindex='+ index +']').tabs('add',{
				title: titleName,
				content: '<iframe class="page-iframe" src="'+ $(this).data('href') +'" frameborder="no" border="no" height="100%" width="100%" scrolling="auto"></iframe>',
				closable: true
			});

        	$(this).parent().closest('li').addClass('active');

			e.stopPropagation();
        });

        //左侧菜单收起
        $(document).on('click', '.toggle-icon', function() {
            $(this).closest("#pf-bd").toggleClass("toggle");
            $(window).resize();
        });

         //关闭当前
	     $('#mm-tabclose').click(function(){
	         var currtab_title = $('#mm').data("currtab");
	         //$(".easyui-tabs1:visible").tabs('close',currtab_title);
	         var $tabs = $(".easyui-tabs1:visible");
	         $tabs.tabs("select", currtab_title);
	         var currentTab = $tabs.tabs("getSelected");
	         $tabs.tabs("update", {
	        	 tab : currentTab,
	        	 options: { }
	         });
	     })
	     //全部关闭
	     $('#mm-tabcloseall').click(function(){
	         $(".easyui-tabs1:visible").find('.tabs li').each(function(i,n){
	             $(".easyui-tabs1:visible").tabs('close', $(n).text());
	         });    
	     });
	     //关闭除当前之外的TAB
	     $('#mm-tabcloseother').click(function(){
	         var currtab_title = $('#mm').data("currtab");
	         $('.tabs-inner span').each(function(i,n){
	             if($(n).text() !== currtab_title)
	                 $(".easyui-tabs1:visible").tabs('close',$(n).text());
	         });    
	     });


        // $(document).on('click', '.pf-modify-pwd', function() {
        //     $('#pf-page').find('iframe').eq(0).attr('src', 'backend/modify_pwd.html')
        // });

        // $(document).on('click', '.pf-notice-item', function() {
        //     $('#pf-page').find('iframe').eq(0).attr('src', 'backend/notice.html')
        // });
	},

	// renderTopMenu
	_createTopMenu: function(){
		var menuStr = '',
			currentIndex = 0;
		for(var i = 0, len = SystemMenu.length; i < len; i++) {
			menuStr += '<li class="pf-nav-item project" data-sort="'+ i +'" data-menu="system_menu_" + i>'+
                      '<a href="javascript:;">'+
                          '<span class="iconfont">'+ SystemMenu[i].icon +'</span>'+
                          '<span class="pf-nav-title">'+ SystemMenu[i].title +'</span>'+
                      '</a>'+
                  '</li>';
            // 渲染当前
            if (SystemMenu[i].isCurrent){
            	currentIndex = i;
            	this._createSiderMenu(SystemMenu[i], i);
            }
		}

		$('.pf-nav').html(menuStr);
		$('.pf-nav-item').eq(currentIndex).addClass('current');
	},

	_createSiderMenu: function(menu, index){
		$('.pf-sider').hide();
		this._createPageContainer(index);
		if($('.pf-sider[arrindex='+ index +']').size() > 0) {
			
			$('.pf-sider[arrindex='+ index +']').show();
			return false;
		};
		var menuStr = '<h2 class="pf-model-name">'+
                    '<span class="iconfont">'+ menu.icon+'</span>'+
                    '<span class="pf-name">'+ menu.title +'</span>'+
                    '<span class="toggle-icon"></span>'+
                '</h2><ul class="sider-nav">';

        for(var i = 0, len = menu.menu.length; i < len; i++){
        	var m = menu.menu[i],
        		mstr = '';
        	var str = '';

        	if(m.isCurrent){
        		if(m.children && m.children.length > 0) {
        			str = '<li class="current">';
        		}else{
        			str = '<li class="current no-child" data-href="'+ m.href +'">';
        		}
        	}else{
        		if(m.children && m.children.length > 0) {
        			str = '<li>';
        		}else{
        			str = '<li class="no-child" data-href="'+ m.href +'">';
        		}
        	}
        	//str = m.isCurrent ? '<li class="current">' : '<li>';

           str += '<a href="javascript:;" class="pf-menu-title">'+
                '<span class="iconfont sider-nav-icon">'+ m.icon +'</span>'+
                '<span class="sider-nav-title">'+ m.title +'</span>'+
                '<i class="iconfont">&#xe642;</i>'+
            '</a>'+
            '<ul class="sider-nav-s">';
            var childStr = '';
            for(var j = 0, jlen = m.children.length; j < jlen; j++){
            	var child = m.children[j];
            	var dataHref = (child.children && child.children.length > 0) ? '' : ('data-href="' + child.href + '"'); 
            	if(child.isCurrent){
            		childStr += '<li class="active ' + (!dataHref ? 'no-child' : '') + '" text="'+ child.title +'" ' + dataHref + '><a href="javascript:void(0);">'+ child.title +'</a>' + this._renderThreeLevelMenu(child.children) + '</li>';
            		$('.easyui-tabs1[arrindex='+ index +']').tabs('add',{
						title: child.title,
						content: '<iframe class="page-iframe" src="'+ child.href +'" frameborder="no" border="no" height="100%" width="100%" scrolling="auto"></iframe>',
						closable: true
					});
            	}else {
            		childStr += '<li class="' + (!dataHref ? 'no-child' : '') + '" text="'+ child.title +'" ' + dataHref + '><a href="javascript:void(0);">'+ child.title +'</a>' + this._renderThreeLevelMenu(child.children) + '</li>';
            	}
            }

            mstr = str + childStr + '</ul></li>';

            menuStr += mstr;

            
        }
        $('.pf-sider-wrap').append($('<div class="pf-sider" arrindex="'+ index +'"></div>').html(menuStr + '</ul>'));

	},

	_renderThreeLevelMenu: function(list) {

		var html = '<ul class="sider-nav-t">',
			i = 0,
			len,
			one;

		if(!list || len === 0) {

			return '';

		}

		len = list.length;

		for(;i<len;i++) {

			one = list[i];

			html += '<li text="' + one.title +'" data-href="' + one.href + '"><a href="#">' + one.title + '</a></li>';

		}

		html += '</ul>';

		return html;

	},

	_createPageContainer: function(index){
		$('.easyui-tabs1').hide();
		if($('.easyui-tabs1[arrindex='+ index +']').size() > 0){
			$('.easyui-tabs1[arrindex='+ index +']').show();
			return false;
		}
		var $tabs = $('<div class="easyui-tabs1" arrindex="'+ index +'" style="width:100%;height:100%;">');
		$('#pf-page').append($tabs);
		$tabs.tabs({
	      tabHeight: 44,
	      onSelect:function(title, index){
	        var currentTab = $tabs.tabs("getSelected");
	        var $active, $parent;
	        if(currentTab.find("iframe") && currentTab.find("iframe").size() && !currentTab.find("iframe").attr("src")){
	            currentTab.find("iframe").attr("src",currentTab.find("iframe").attr("src"));
	        }
	        currentTab.find("iframe").resize();
	        $('.pf-sider:visible').find('.sider-nav-s li').removeClass('active');
	        var $active = $('.pf-sider:visible').find('.sider-nav-s li[text='+ title +']').addClass('active');
	        var $parent = $active.parent();
	        if($parent.hasClass('sider-nav-t')) {
	        	$parent.closest('li').addClass('active');
	        }
	      }
	    });

	    $tabs.find('.tabs-header').on('contextmenu', function(e){
	    	e.preventDefault();
	    	if($(e.target).closest('li').size() > 0 || $(e.target).is('li')){
	    		$('#mm').menu('show', {
		             left: e.pageX,
		             top: e.pageY,
		         });
	    		var subtitle = $(e.target).closest('li').size() ? $(e.target).closest('li') : $(e.target);
        		$('#mm').data("currtab",subtitle.text());
	    	}
	    })
	}

};