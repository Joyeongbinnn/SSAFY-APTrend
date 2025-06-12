// icons
import {
  DashboardOutlined,
  EnvironmentOutlined,
  FileTextOutlined,
  LineChartOutlined,
  StarOutlined,
  ChromeOutlined,
  QuestionOutlined
} from '@ant-design/icons-vue';

const sidebarItem = [
  { header: 'APTTrend 메뉴' },

  {
    title: 'Main',
    icon: DashboardOutlined,
    to: '/'
  },
  {
    title: 'APT Map',
    icon: EnvironmentOutlined,
    to: '/map'
  },
  {
    title: 'Report',
    icon: FileTextOutlined,
    to: '/issues'
  },
  {
    title: '시세 예측',
    icon: LineChartOutlined,
    to: '/predict'
  },
  {
    title: 'APT Review',
    icon: StarOutlined,
    to: '/review'
  },

  { divider: true },
  { header: '기타 기능' },

  {
    title: '샘플 페이지',
    icon: ChromeOutlined,
    to: '/sample-page'
  },
  {
    title: '문서',
    icon: QuestionOutlined,
    to: 'https://edu.ssafy.com/',
    type: 'external',
    chip: 'gitbook',
    chipColor: 'secondary',
    chipVariant: 'flat'
  }
];

export default sidebarItem;
