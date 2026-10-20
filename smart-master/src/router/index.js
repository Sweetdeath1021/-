// 导入组件
import Vue from 'vue';
import Router from 'vue-router';
// 登录
// import login from '@/views/login';
//注册
// import register from '@/views/register';
// const login = () => import('@/views/login')

// const register = () => import('@/views/register')
// 首页
// import home from '@/views/home';
// const index = () => import('@/views/index')

// import personal from '@/views/personal/index.vue'
// import index from '@/views/index.vue'

// import examination from '@/views/examination/index.vue'
// import upload from '@/views/examination/upload.vue'
// import editor from '@/views/examination/photoEditor.vue'

// import task from '@/views/task/index.vue'
// import list from '@/views/task/list.vue'
// import distribute from '@/views/task/details.vue'
// 启用路由
Vue.use(Router);

// 导出路由
export default new Router({
    routes: [{
        path: '/',
        name: '',
        component: () => import('@/views/login'),
        redirect: '/login',
        hidden: true,
        meta: {
            requireAuth: false
        }
    }, {
        path: '/login',
        name: '登录',
        component: () => import('@/views/login'),
        hidden: true,
        meta: {
            requireAuth: false
        }
    }, {
        path: '/register',
        name: '注册',
        component: () => import('@/views/register'),
        hidden: true,
        meta: {
            requireAuth: false
        }
    },
    {
        path: '/home',
        name: '首页',
        component: () => import('@/views/home'),
        children: [{
            path: '/personal',
            name: '个人中心',
            component: () => import('@/views/personal/index.vue'),
            meta: {
                requireAuth: true
            }
        }, {
            path: '/index',
            name: '主页',
            component: () => import('@/views/index.vue'),
            meta: {
                requireAuth: true
            }
        }, {
            path: '/examination',
            name: '试卷管理',
            component: () => import('@/views/examination/index.vue'),
            meta: {
                requireAuth: true
            },
            children: [{
                path: '/examination/upload',
                name: '上传试卷',
                component: () => import('@/views/examination/upload.vue'),
                meta: {
                    requireAuth: true
                }
            }]
        }, {
            path: '/task',
            name: '阅卷管理',
            component: () => import('@/views/task/index.vue'),
            meta: {
                requireAuth: true
            },
            children: [{
                path: '/task/list',
                name: '任务分配',
                component: () => import('@/views/task/list.vue'),
                meta: {
                    requireAuth: true
                }
            }, {
                path: '/task/distribute',
                name: '划分题目',
                component: () => import('@/views/task/distribute.vue'),
                meta: {
                    requireAuth: true
                }
            }, {
                path: '/task/details',
                name: '阅卷详情',
                component: () => import('@/views/task/details.vue'),
                meta: {
                    requireAuth: true
                }
            }]
        },
        {
            path: '/manageAnalysis',
            name: '组长学情分析',
            component: () => import('@/views/studyAnalysis/index.vue'),
            meta: {
                requireAuth: true
            },
            children: []
        },
        {
            path: '/teacherMark',
            name: '老师阅卷',
            component: () => import('@/views/teacherMark/index.vue'),
            meta: {
                requireAuth: true
            },
            children: [{
                path: '/teacherMark/startMark',
                name: '开始阅卷',
                component: () => import('@/views/teacherMark/startMark.vue'),
                meta: {
                    requireAuth: true
                }
            }]
        },
        {
            path: '/studentTable',
            name: '学生页面',
            component: () => import('@/views/studentTable/index.vue'),
            meta: {
                requireAuth: true
            },
            children: [{
                path: '/studentTable/table',
                name: '学生表格',
                component: () => import('@/views/studentTable/table.vue'),
                meta: {
                    requireAuth: true
                }
            }, {
                path: '/studentTable/synthesize',
                name: '综合分析',
                component: () => import('@/views/studentTable/synthesize.vue'),
                meta: {
                    requireAuth: true
                }
            },]
        }, {
            path: '/teacherAnalysis',
            name: '老师学情分析',
            component: () => import('@/views/studyAnalysis/index.vue'),
            meta: {
                requireAuth: true
            },
            children: []
        },
        {
            path: '/multipleRemark',
            name: '多样评语',
            component: () => import('@/views/multipleRemark/index.vue'),
            meta: {
                requireAuth: true
            }
        }, {
            path: '/studentLook',
            name: '试卷查看',
            component: () => import('@/views/studentLook/index.vue'),
            meta: {
                requireAuth: true
            }
        }, {
            path: '/knowledgeExtension',
            name: '知识延展',
            component: () => import('@/views/knowledgeExtension/index.vue'),
            meta: {
                requireAuth: true
            }
        },
        ]
    },
    {
        path: '/grading',
        name: '阅卷页面',
        component: () => import('@/views/grading.vue'),
        meta: {
            requireAuth: true
        }
    },
    ]
})